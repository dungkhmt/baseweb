package com.hust.baseweb.test.simulator;

import com.google.gson.Gson;
import com.hust.baseweb.applications.logistics.entity.Facility;
import com.hust.baseweb.applications.logistics.model.ExportInventoryItemInputModel;
import com.hust.baseweb.applications.logistics.model.ExportInventoryItemsInputModel;
import com.hust.baseweb.applications.logistics.model.InventoryModel;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Random;

@Getter
@Setter
public class ExportFacilityAgent extends Thread {
    public static final String module = ExportFacilityAgent.class.getName();

    private Random rand = new Random();
    private Thread thread = null;
    private String token;
    private String username;
    private String password;

    private HttpPostExecutor executor = new HttpPostExecutor();

    private int nbIters = 10;
    private int idleTime = 360;

    public ExportFacilityAgent(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public void run() {
        System.out.println(module + "::run....");

        token = Login.login(username, password);

        createShipments();

    }

    public void createAShipment() throws Exception {
        // get first Page<InventoryModel.OrderHeader> (call API /get-inventory-order-header/page)
        String response = executor.execGetUseToken(Constants.URL_ROOT +
                "/get-inventory-order-header/page?size=5&page=0",
            null,
            token);
        Gson gson = new Gson();
        Page page = gson.fromJson(response, Page.class);

        // select random a page index (pid)
        int randomPageIndex = rand.nextInt(page.getTotalPages());

        // get the pid(th) Page<InventoryModel.OrderHeader> (call API /get-inventory-order-header/page)
        response = executor.execGetUseToken(Constants.URL_ROOT +
                "/get-inventory-order-header/page?size=5&page=" + randomPageIndex,
            null,
            token);
        page = gson.fromJson(response, Page.class);
        List content = page.getContent();

        // select randomly an order from the page
        int randomOrderId = rand.nextInt(5);
        InventoryModel.OrderHeader orderHeader = (InventoryModel.OrderHeader) (content.get(randomOrderId));

        FacilityManager facilityManager = new FacilityManager(token);
        List<Facility> facilities = facilityManager.getListFacility();
        Facility randomFacility = facilities.get(rand.nextInt(facilities.size()));

        response = executor.execPostUseToken(Constants.URL_ROOT +
                "/api/get-inventory-order-detail",
            "{\"orderId\":\"" +
                orderHeader.getOrderId() +
                "\",\"facilityId\":\"" +
                randomFacility.getFacilityId() +
                "\"}",
            token);
        List<InventoryModel.OrderItem> orderItems = gson.fromJson(response, List.class);

        // enter randomly the quantity to be exported for each order-item
        ExportInventoryItemsInputModel exportInventoryItemsInputModel = new ExportInventoryItemsInputModel();
        ExportInventoryItemInputModel[] exportInventoryItemInputModels = orderItems.stream()
            .filter(orderItem -> orderItem.getQuantity() - orderItem.getExportedQuantity() <
                orderItem.getInventoryQuantity())
            .map(orderItem -> {
                ExportInventoryItemInputModel exportInventoryItemInputModel = new ExportInventoryItemInputModel();
                exportInventoryItemInputModel.setFacilityId(randomFacility.getFacilityId());
                exportInventoryItemInputModel.setOrderId(orderItem.getOrderId());
                exportInventoryItemInputModel.setQuantity(rand.nextInt((Math.min(orderItem.getQuantity() -
                    orderItem.getExportedQuantity(), orderItem.getInventoryQuantity())) - 1) + 1);
                exportInventoryItemInputModel.setOrderItemSeqId(orderItem.getOrderItemSeqId());
                return exportInventoryItemInputModel;
            }).toArray(ExportInventoryItemInputModel[]::new);
        exportInventoryItemsInputModel.setInventoryItems(exportInventoryItemInputModels);

        // perform export (call API /export-inventory-items)
        executor.execPostUseToken(Constants.URL_ROOT +
                "/api/export-inventory-items",
            gson.toJson(exportInventoryItemsInputModel),
            token);
    }

    public void createShipments() {
        // TODO:
        for (int i = 0; i < nbIters; i++) {
            try {
                createAShipment();

                Thread.sleep(idleTime);

            } catch (Exception e) {
                e.printStackTrace();
                break;
            }
        }


    }
}

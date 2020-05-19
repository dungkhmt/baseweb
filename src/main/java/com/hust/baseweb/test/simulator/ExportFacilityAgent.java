package com.hust.baseweb.test.simulator;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hust.baseweb.applications.logistics.entity.Facility;
import com.hust.baseweb.applications.logistics.model.ExportInventoryItemInputModel;
import com.hust.baseweb.applications.logistics.model.ExportInventoryItemsInputModel;
import com.hust.baseweb.applications.logistics.model.InventoryModel;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.PageImpl;

import java.util.ArrayList;
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
                "/api/get-inventory-order-header/page?size=5&page=0",
            null,
            token);

        Gson gson = new Gson();

        ObjectMapper objectMapper = new ObjectMapper();

//        new InstanceCreator<PageImpl<InventoryModel.OrderHeader>>()
        PageImpl<InventoryModel.OrderHeader> pageInventoryOrder = objectMapper.readValue(response,
            new TypeReference<RestResponsePage<InventoryModel.OrderHeader>>() {
            });
//        PageImpl<InventoryModel.OrderHeader> pageInventoryOrder = gson.fromJson(response,
//            new TypeToken<PageImpl<InventoryModel.OrderHeader>>() {
//            }.getType());

        // select random a page index (pid)
        int randomPageIndex = rand.nextInt(pageInventoryOrder.getTotalPages());

        // get the pid(th) Page<InventoryModel.OrderHeader> (call API /get-inventory-order-header/page)
        response = executor.execGetUseToken(Constants.URL_ROOT +
                "/api/get-inventory-order-header/page?size=5&page=" + randomPageIndex,
            null,
            token);
        pageInventoryOrder = objectMapper.readValue(response,
            new TypeReference<RestResponsePage<InventoryModel.OrderHeader>>() {
            });
        List<InventoryModel.OrderHeader> content = pageInventoryOrder.getContent();

        // select randomly an order from the page
        int randomOrderId = rand.nextInt(5);
        InventoryModel.OrderHeader orderHeader = content.get(randomOrderId);

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
        List<InventoryModel.OrderItem> orderItems = gson.fromJson(response,
            new TypeToken<ArrayList<InventoryModel.OrderItem>>() {
            }.getType());

        // enter randomly the quantity to be exported for each order-item
        ExportInventoryItemsInputModel exportInventoryItemsInputModel = new ExportInventoryItemsInputModel();
        ExportInventoryItemInputModel[] exportInventoryItemInputModels = orderItems.stream()
            .filter(orderItem ->
                orderItem.getQuantity() > orderItem.getExportedQuantity() &&
                    orderItem.getQuantity() - orderItem.getExportedQuantity() <
                        orderItem.getInventoryQuantity())
            .map(orderItem -> {
                ExportInventoryItemInputModel exportInventoryItemInputModel = new ExportInventoryItemInputModel();
                exportInventoryItemInputModel.setFacilityId(randomFacility.getFacilityId());
                exportInventoryItemInputModel.setOrderId(orderItem.getOrderId());
                exportInventoryItemInputModel.setQuantity(rand.nextInt((Math.min(orderItem.getQuantity() -
                    orderItem.getExportedQuantity(), orderItem.getInventoryQuantity()))) + 1);
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

package com.hust.baseweb.test.simulator;

import com.google.gson.Gson;
import com.hust.baseweb.applications.logistics.entity.Facility;
import com.hust.baseweb.applications.logistics.entity.Product;
import com.hust.baseweb.applications.logistics.model.ImportInventoryItemInputModel;
import com.hust.baseweb.applications.logistics.model.ImportInventoryItemsInputModel;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
@Getter
@Setter
public class ImportFacilityAgent extends Thread {
    public static final String module = ImportFacilityAgent.class.getName();

    private Random rand = new Random();
    private Thread thread = null;
    private String token;
    private String username;
    private String password;

    private HttpPostExecutor executor = new HttpPostExecutor();

    private int nbIters = 10;
    private int idleTime = 360;

    public ImportFacilityAgent(String username, String password){
        this.username = username; this.password = password;
    }
    public void run() {
        System.out.println(module + "::run....");

        token = Login.login(username, password);

        createReceipts();

    }
    public double createAReceipt(List<Product> products, List<Facility> facilities) throws Exception{
        try{
            Gson gson = new Gson();
            ImportInventoryItemsInputModel input = null;
            List<ImportInventoryItemInputModel> list = new ArrayList<>();
            Facility facility = facilities.get(rand.nextInt(list.size()));
            for(Product p: products){
                ImportInventoryItemInputModel item = new ImportInventoryItemInputModel();
                item.setFacilityId(facility.getFacilityId());
                item.setLotId(null);
                item.setProductId(p.getProductId());
                item.setQuantityOnHandTotal(rand.nextInt(1000)+1);
                list.add(item);
            }
            ImportInventoryItemInputModel[] arr = new ImportInventoryItemInputModel[list.size()];
            for(int i = 0; i < list.size(); i++) arr[i] = list.get(i);
            input.setInventoryItems(arr);

            double t0 = System.currentTimeMillis();
            String json = gson.toJson(input);
            String rs = executor.execPostUseToken(Constants.URL_ROOT + "/api/import-inventory-items",
                    json,
                    token);
            //System.out.println(module + "::createOrder, rs = " + rs);
            return System.currentTimeMillis() - t0;
        }catch(Exception e){
            e.printStackTrace();
            throw new Exception(e);
        }
    }
    public void createReceipts(){
        FacilityManager facilityManager = new FacilityManager(token);
        ProductManager productManager = new ProductManager(token);
        List<Facility> facilities = facilityManager.getListFacility();
        List<Product> products = productManager.getProducts();

        for(int i = 0; i < nbIters; i++){
            try{
                double time = createAReceipt(products,facilities);
                Thread.sleep(idleTime);
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }
}
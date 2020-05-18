package com.hust.baseweb.test.simulator;

import lombok.Getter;
import lombok.Setter;

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

    public ExportFacilityAgent(String username, String password){
        this.username = username; this.password = password;
    }
    public void run() {
        System.out.println(module + "::run....");

        token = Login.login(username, password);

        createShipments();

    }
    public void createAShipment() throws Exception{
        // get first Page<InventoryModel.OrderHeader> (call API /get-inventory-order-header/page)

        // select random a page index (pid)

        // get the pid(th) Page<InventoryModel.OrderHeader> (call API /get-inventory-order-header/page)

        // select randomly an order from the page

        // enter randomly the quantity to be exported for each order-item

        // perform export (call API /export-inventory-items)
    }
    public void createShipments(){
        // TODO:
        for(int i = 0; i < nbIters; i++) {
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

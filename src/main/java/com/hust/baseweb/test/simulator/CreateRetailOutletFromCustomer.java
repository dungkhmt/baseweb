package com.hust.baseweb.test.simulator;


import com.google.gson.Gson;
import com.hust.baseweb.applications.customer.entity.PartyCustomer;
import com.hust.baseweb.applications.customer.model.CreateRetailOutletInputModel;
import com.hust.baseweb.applications.geo.entity.PostalAddress;

import java.util.List;
import java.util.Random;

public class CreateRetailOutletFromCustomer {

    private Random rand = new Random();
    private Thread thread = null;
    private String token;
    private HttpPostExecutor executor = new HttpPostExecutor();

    public void run() {

        token = Login.login("admin", "123");

        CustomerManager customerManager = new CustomerManager(token);
        List<PartyCustomer> customers = customerManager.getCustomers();
        for (PartyCustomer c : customers) {
            String customerCode = c.getCustomerCode();
            String customerName = c.getCustomerName();
            PostalAddress addr = c.getPostalAddress().get(0);
            String address = addr.getAddress();
            Double lat = (addr.getGeoPoint() != null ? addr.getGeoPoint().getLatitude() : null);
            Double lng = (addr.getGeoPoint() != null ? addr.getGeoPoint().getLongitude() : null);
//            System.out.println(c.getCustomerCode() + "\t" + c.getCustomerName() + "\t" + address + "\t" + lat + "," + lng);

            if (lat != null) {
                CreateRetailOutletInputModel input = new CreateRetailOutletInputModel();
                input.setRetailOutletName(customerName);
                input.setRetailOutletCode(customerCode);
                input.setAddress(address);
                input.setLatitude(lat);
                input.setLongitude(lng);

                Gson gson = new Gson();
                String json = gson.toJson(input);
                try {
                    String rs = executor.execPostUseToken(
                        Constants.URL_ROOT + "/api/create-retail-outlet",
                        json,
                        token);
//                    System.out.println("Create retail outlet, rs = " + rs);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) {

        CreateRetailOutletFromCustomer app = new CreateRetailOutletFromCustomer();

        app.run();

    }
}

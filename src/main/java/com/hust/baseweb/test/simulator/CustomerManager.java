package com.hust.baseweb.test.simulator;

import com.google.gson.Gson;
import com.hust.baseweb.applications.customer.entity.PartyCustomer;
import com.hust.baseweb.applications.customer.model.GetListCustomerOutputModel;
import com.hust.baseweb.applications.customer.model.PartyCustomerModel;

import java.util.ArrayList;
import java.util.List;

public class CustomerManager {
    private HttpPostExecutor executor = new HttpPostExecutor();
    private String token;
    public CustomerManager(String token){
        this.token = token;
    }

    public List<PartyCustomer> getCustomers() {
        try {
            String json = "{\"statusId\":null}";

            //String rs = executor.execPostUseToken(Constants.URL_ROOT + "/api/get-list-party-customers", json, token);
            //String rs = executor.execGetUseToken(Constants.URL_ROOT + "/api/get-list-party-customers", null, token);
            String rs = executor.execPostUseToken(Constants.URL_ROOT + "/api/get-list-customer", json, token);
            System.out.println("::getCustomers, rs = " + rs);
            Gson gson = new Gson();
            //GetListPartyCustomerOutputModel customers = gson.fromJson(rs, GetListPartyCustomerOutputModel.class);
            //return customers.getCustomers();
            GetListCustomerOutputModel listCustomers = gson.fromJson(rs,GetListCustomerOutputModel.class);
            List<PartyCustomer> lst = listCustomers.getLists();

            return lst;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}

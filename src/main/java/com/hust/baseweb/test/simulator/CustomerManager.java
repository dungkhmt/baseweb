package com.hust.baseweb.test.simulator;

import com.google.gson.Gson;
import com.hust.baseweb.applications.customer.entity.PartyCustomer;
import com.hust.baseweb.applications.customer.entity.PartyDistributor;
import com.hust.baseweb.applications.customer.entity.PartyRetailOutlet;
import com.hust.baseweb.applications.customer.model.GetListCustomerOutputModel;
import com.hust.baseweb.applications.customer.model.GetListDistributorOutPutModel;
import com.hust.baseweb.applications.customer.model.GetListRetailOutletOutputModel;

import java.util.List;

public class CustomerManager {
    private HttpPostExecutor executor = new HttpPostExecutor();
    private String token;

    public CustomerManager(String token) {
        this.token = token;
    }

    public List<PartyCustomer> getCustomers() {
        try {
            String json = "{\"statusId\":null}";

            //String rs = executor.execPostUseToken(Constants.URL_ROOT + "/api/get-list-party-customers", json, token);
            //String rs = executor.execGetUseToken(Constants.URL_ROOT + "/api/get-list-party-customers", null, token);
            String rs = executor.execPostUseToken(Constants.URL_ROOT + "/api/get-list-customer", json, token);
//            System.out.println("::getCustomers, rs = " + rs);
            Gson gson = new Gson();
            //GetListPartyCustomerOutputModel customers = gson.fromJson(rs, GetListPartyCustomerOutputModel.class);
            //return customers.getCustomers();
            GetListCustomerOutputModel listCustomers = gson.fromJson(rs, GetListCustomerOutputModel.class);
            List<PartyCustomer> lst = listCustomers.getLists();

            return lst;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<PartyDistributor> getDistributors() {
        try {
            String json = "{\"statusId\":null}";

            //String rs = executor.execPostUseToken(Constants.URL_ROOT + "/api/get-list-party-customers", json, token);
            //String rs = executor.execGetUseToken(Constants.URL_ROOT + "/api/get-list-party-customers", null, token);
            String rs = executor.execPostUseToken(Constants.URL_ROOT + "/api/get-list-distributor", json, token);
//            System.out.println("::getDistributors, rs = " + rs);
            Gson gson = new Gson();
            //GetListPartyCustomerOutputModel customers = gson.fromJson(rs, GetListPartyCustomerOutputModel.class);
            //return customers.getCustomers();
            GetListDistributorOutPutModel listDistributors = gson.fromJson(rs, GetListDistributorOutPutModel.class);
            List<PartyDistributor> lst = listDistributors.getLists();

            return lst;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<PartyRetailOutlet> getRetailOutlets() {
        try {
            String json = "{\"statusId\":null}";

            //String rs = executor.execPostUseToken(Constants.URL_ROOT + "/api/get-list-party-customers", json, token);
            //String rs = executor.execGetUseToken(Constants.URL_ROOT + "/api/get-list-party-customers", null, token);
            String rs = executor.execPostUseToken(Constants.URL_ROOT + "/api/get-list-retail-outlet", json, token);
//            System.out.println("::getRetailOutlets, rs = " + rs);
            Gson gson = new Gson();
            //GetListPartyCustomerOutputModel customers = gson.fromJson(rs, GetListPartyCustomerOutputModel.class);
            //return customers.getCustomers();
            GetListRetailOutletOutputModel list = gson.fromJson(rs, GetListRetailOutletOutputModel.class);
            List<PartyRetailOutlet> lst = list.getList();

            return lst;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}

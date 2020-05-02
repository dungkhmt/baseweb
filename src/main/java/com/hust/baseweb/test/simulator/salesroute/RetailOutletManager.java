package com.hust.baseweb.test.simulator.salesroute;

import com.google.gson.Gson;
import com.hust.baseweb.applications.customer.entity.PartyCustomer;
import com.hust.baseweb.applications.customer.entity.PartyRetailOutlet;
import com.hust.baseweb.applications.customer.model.GetListCustomerOutputModel;
import com.hust.baseweb.applications.customer.model.GetListRetailOutletOutputModel;
import com.hust.baseweb.applications.sales.model.retailoutletsalesmandistributor.GetListRetailOutletsOfSalesmanAndDistributorInputModel;
import com.hust.baseweb.test.simulator.Constants;
import com.hust.baseweb.test.simulator.HttpPostExecutor;

import java.util.List;
import java.util.UUID;

public class RetailOutletManager {
    private HttpPostExecutor executor = new HttpPostExecutor();
    private String token;
    public RetailOutletManager(String token){
        this.token = token;
    }

    public List<PartyRetailOutlet> getRetailoutlets() {
        try {
            String json = "{\"statusId\":null}";

            //String rs = executor.execPostUseToken(Constants.URL_ROOT + "/api/get-list-party-customers", json, token);
            //String rs = executor.execGetUseToken(Constants.URL_ROOT + "/api/get-list-party-customers", null, token);
            String rs = executor.execPostUseToken(Constants.URL_ROOT + "/api/get-list-retail-outlet", json, token);
            System.out.println("::getRetailoutlets, rs = " + rs);
            Gson gson = new Gson();
             GetListRetailOutletOutputModel listRetailOutlets = gson.fromJson(rs,GetListRetailOutletOutputModel.class);
            List<PartyRetailOutlet> lst = listRetailOutlets.getList();

            return lst;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public List<PartyRetailOutlet> getRetailoutletsOfSalesmanAndDistributor(UUID partySalesmanId, UUID partyDistributorId) {
        try {
            Gson gson = new Gson();
            GetListRetailOutletsOfSalesmanAndDistributorInputModel in  = new GetListRetailOutletsOfSalesmanAndDistributorInputModel(partySalesmanId, partyDistributorId);

            String json = gson.toJson(in);

            //String rs = executor.execPostUseToken(Constants.URL_ROOT + "/api/get-list-party-customers", json, token);
            //String rs = executor.execGetUseToken(Constants.URL_ROOT + "/api/get-list-party-customers", null, token);
            String rs = executor.execPostUseToken(Constants.URL_ROOT + "/api/get-list-retail-outlets-of-salesman-and-distributor", json, token);
            System.out.println("::getRetailoutletsOfSalesmanAndDistributor, rs = " + rs);

            GetListRetailOutletOutputModel listRetailOutlets = gson.fromJson(rs,GetListRetailOutletOutputModel.class);
            List<PartyRetailOutlet> lst = listRetailOutlets.getList();

            return lst;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}


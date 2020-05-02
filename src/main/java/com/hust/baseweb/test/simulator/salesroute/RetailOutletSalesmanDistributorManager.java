package com.hust.baseweb.test.simulator.salesroute;

import com.google.gson.Gson;
import com.hust.baseweb.applications.sales.entity.RetailOutletSalesmanVendor;
import com.hust.baseweb.applications.sales.model.retailoutletsalesmandistributor.RetailOutletSalesmanDistributorInputModel;
import com.hust.baseweb.test.simulator.Constants;
import com.hust.baseweb.test.simulator.HttpPostExecutor;

import java.util.UUID;

public class RetailOutletSalesmanDistributorManager {
    private HttpPostExecutor executor = new HttpPostExecutor();
    private String token;
    public RetailOutletSalesmanDistributorManager(String token){
        this.token = token;
    }

    public RetailOutletSalesmanVendor getRetailOutletSalesmanDistributor(UUID retailOutletId, UUID salesmanId, UUID distributorId){
        try{
            RetailOutletSalesmanDistributorInputModel in = new RetailOutletSalesmanDistributorInputModel();
            in.setPartyDistributorId(distributorId);
            in.setPartyRetailOutletId(retailOutletId);
            in.setPartySalesmanId(salesmanId);

            Gson gson = new Gson();
            String json = gson.toJson(in);

            String rs = executor.execPostUseToken(Constants.URL_ROOT + "/api/get-retail-outlet-salesman-distributor", json, token);

            RetailOutletSalesmanVendor retailOutletSalesmanVendor = gson.fromJson(rs,RetailOutletSalesmanVendor.class);
            return retailOutletSalesmanVendor;

        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }
}

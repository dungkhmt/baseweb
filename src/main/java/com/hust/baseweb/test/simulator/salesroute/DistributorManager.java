package com.hust.baseweb.test.simulator.salesroute;

import com.google.gson.Gson;
import com.hust.baseweb.applications.customer.entity.PartyDistributor;
import com.hust.baseweb.applications.customer.model.GetListDistributorOutPutModel;
import com.hust.baseweb.applications.sales.model.distributor.ListDistributorOutputModel;
import com.hust.baseweb.applications.sales.model.salesmandistributor.GetListDistributorsOfSalesmanInputModel;
import com.hust.baseweb.test.simulator.Constants;
import com.hust.baseweb.test.simulator.HttpPostExecutor;

import java.util.List;
import java.util.UUID;

public class DistributorManager {
    private HttpPostExecutor executor = new HttpPostExecutor();
    private String token;

    public DistributorManager(String token) {
        this.token = token;
    }

    public List<PartyDistributor> getListDistributors() {
        try {
            String json = "{\"statusId\":null}";

            String rs = executor.execPostUseToken(Constants.URL_ROOT + "/api/get-list-distributor", json, token);
//            System.out.println("::getListDistributors, rs = " + rs);
            Gson gson = new Gson();
            GetListDistributorOutPutModel list = gson.fromJson(rs, GetListDistributorOutPutModel.class);
            List<PartyDistributor> lst = list.getLists();

            return lst;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<PartyDistributor> getListDistributorsOfSalesman(UUID partySalesmanId) {
        try {
            Gson gson = new Gson();
            GetListDistributorsOfSalesmanInputModel in = new GetListDistributorsOfSalesmanInputModel(partySalesmanId);
            //in.setPartySalesmanId(partySalesmanId);
            String json = gson.toJson(in);
//            System.out.println("::getListDistributorsOfSalesman, input json = " + json);
            String rs = executor.execPostUseToken(Constants.URL_ROOT + "/api/get-distributors-of-salesman",
                json,
                token);


            ListDistributorOutputModel list = gson.fromJson(rs, ListDistributorOutputModel.class);
            List<PartyDistributor> lst = list.getPartyDistributorList();
//            System.out.println("::getListDistributorsOfSalesman, rs = " + rs + ", returned lst.sz = " +
//                    (lst != null ? lst.size(): " NULL"));
            return lst;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}

package com.hust.baseweb.test.simulator.salesroute;

import com.google.gson.Gson;
import com.hust.baseweb.applications.sales.model.ListSalesmanOutputModel;
import com.hust.baseweb.applications.sales.model.salesman.SalesmanOutputModel;
import com.hust.baseweb.test.simulator.Constants;
import com.hust.baseweb.test.simulator.HttpPostExecutor;

import java.util.List;

public class SalesmanManager {
    private HttpPostExecutor executor = new HttpPostExecutor();
    private String token;

    public SalesmanManager(String token) {
        this.token = token;
    }

    List<SalesmanOutputModel> getListSalesman() {
        try {
            String json = "{\"statusId\":null}";

            String rs = executor.execPostUseToken(Constants.URL_ROOT + "/api/get-list-all-salesmans", json, token);
//            System.out.println("::getListSalesman, rs = " + rs);
            Gson gson = new Gson();
            ListSalesmanOutputModel out = gson.fromJson(rs, ListSalesmanOutputModel.class);
            List<SalesmanOutputModel> salesman = out.getList();
            return salesman;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}

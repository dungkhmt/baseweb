package com.hust.baseweb.test.simulator.salesroute;

import com.google.gson.Gson;
import com.hust.baseweb.applications.salesroutes.entity.SalesRouteVisitFrequency;
import com.hust.baseweb.applications.salesroutes.model.salesroutevisitfrequency.ListSalesRouteVisitFrequencyOutputModel;
import com.hust.baseweb.test.simulator.Constants;
import com.hust.baseweb.test.simulator.HttpPostExecutor;

import java.util.List;

public class SalesRouteVisitFrequencyManager {

    private HttpPostExecutor executor = new HttpPostExecutor();
    private String token;

    public SalesRouteVisitFrequencyManager(String token) {
        this.token = token;
    }

    public List<SalesRouteVisitFrequency> getListSalesRouteVisitFrequency() {
        try {
            String rs = executor.execGetUseToken(
                Constants.URL_ROOT + "/api/get-list-sales-route-visit-frequency",
                "",
                token);
//            System.out.println("::getListSalesRouteVisitFrequency, rs = " + rs);
            Gson gson = new Gson();
            ListSalesRouteVisitFrequencyOutputModel out = gson.fromJson(
                rs,
                ListSalesRouteVisitFrequencyOutputModel.class);
            return out.getList();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}

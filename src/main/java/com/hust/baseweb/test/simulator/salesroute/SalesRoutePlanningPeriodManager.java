package com.hust.baseweb.test.simulator.salesroute;

import com.google.gson.Gson;
import com.hust.baseweb.applications.salesroutes.entity.SalesRoutePlanningPeriod;
import com.hust.baseweb.applications.salesroutes.model.salesrouteplanningperiod.GetSalesRoutePlanningPeriodOutputModel;
import com.hust.baseweb.test.simulator.Constants;
import com.hust.baseweb.test.simulator.HttpPostExecutor;

import java.util.List;

public class SalesRoutePlanningPeriodManager {
    private HttpPostExecutor executor = new HttpPostExecutor();
    private String token;

    public SalesRoutePlanningPeriodManager(String token){
        this.token = token;
    }

    public List<SalesRoutePlanningPeriod> getListSalesRoutePlanningPeriods(){
        try{
            String json = "{\"statusId\":null}";

            String rs = executor.execPostUseToken(Constants.URL_ROOT + "/api/get-list-sales-route-planning-period", json, token);
            System.out.println("::getListSalesRoutePlanningPeriods, rs = " + rs);
            Gson gson = new Gson();
            GetSalesRoutePlanningPeriodOutputModel out = gson.fromJson(rs, GetSalesRoutePlanningPeriodOutputModel.class);
            return out.getSalesRoutePlanningPeriodList();
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }
}

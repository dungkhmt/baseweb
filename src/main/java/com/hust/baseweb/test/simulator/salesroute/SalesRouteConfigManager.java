package com.hust.baseweb.test.simulator.salesroute;

import com.google.gson.Gson;
import com.hust.baseweb.applications.salesroutes.entity.SalesRouteConfig;
import com.hust.baseweb.applications.salesroutes.model.salesrouteconfig.GetListSalesRouteConfigOutputModel;
import com.hust.baseweb.test.simulator.Constants;
import com.hust.baseweb.test.simulator.HttpPostExecutor;

import java.util.List;

public class SalesRouteConfigManager {
    private HttpPostExecutor executor = new HttpPostExecutor();
    private String token;

    public SalesRouteConfigManager(String token){
        this.token = token;
    }
    public List<SalesRouteConfig> getListSalesRouteConfigs(){
        try{
            String json = "{\"statusId\":null}";

            String rs = executor.execPostUseToken(Constants.URL_ROOT + "/api/get-list-sales-route-config", json, token);
            System.out.println("::getListSalesRouteConfigs, rs = " + rs);
            Gson gson = new Gson();

            GetListSalesRouteConfigOutputModel out = gson.fromJson(rs,GetListSalesRouteConfigOutputModel.class);
            return out.getSalesRouteConfigList();
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }
}

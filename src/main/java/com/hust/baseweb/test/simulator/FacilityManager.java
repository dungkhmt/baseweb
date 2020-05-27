package com.hust.baseweb.test.simulator;

import com.google.gson.Gson;
import com.hust.baseweb.applications.logistics.entity.Facility;
import com.hust.baseweb.applications.logistics.model.GetListFacilityOutputModel;

import java.util.List;

public class FacilityManager {

    private HttpPostExecutor executor = new HttpPostExecutor();
    private String token;

    public FacilityManager(String token) {

        this.token = token;
    }

    public List<Facility> getListFacility() {

        String json = "{\"statusId\":null}";
        String rs = null;
        Gson gson = new Gson();
        GetListFacilityOutputModel list;
        try {
            rs = executor.execPostUseToken(Constants.URL_ROOT + "/api/get-list-facility", json, token);
//            System.out.println("::getListFacility, rs = " + rs);
            list = gson.fromJson(rs, GetListFacilityOutputModel.class);
            return list.getFacilities();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}

package com.hust.baseweb.test.simulator;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hust.baseweb.applications.customer.model.PartyCustomerModel;

import java.util.List;

public class PartyManager {
    private HttpPostExecutor executor = new HttpPostExecutor();
    private String token;

    public PartyManager(String token) {
        this.token = token;
    }

    public List<PartyCustomerModel> getListParty() {
        try {
            String rs = executor.execGetUseToken(Constants.URL_ROOT + "/api/get-list-party-customers", "", token);
            Gson gson = new Gson();
            return gson.fromJson(rs, new TypeToken<List<PartyCustomerModel>>() {
            }.getType());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}

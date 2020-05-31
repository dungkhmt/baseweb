package com.hust.baseweb.applications.tracklocations.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserLocationModel {

    private String userLoginId;
    private double lat;
    private double lng;

    public UserLocationModel() {
        super();

    }

    public UserLocationModel(String userLoginId, double lat, double lng) {
        super();
        this.userLoginId = userLoginId;
        this.lat = lat;
        this.lng = lng;
    }

}

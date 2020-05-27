package com.hust.baseweb.applications.customer.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateRetailOutletInputModel {

    private String retailOutletCode;
    private String retailOutletName;
    private String address;
    private Double latitude;
    private Double longitude;
}

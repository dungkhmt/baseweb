package com.hust.baseweb.applications.postsys.model.postshiporder;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreatePostShipOrderInputModel {

    private boolean fromCustomerExist;
    private String fromCustomerId;
    private String fromCustomerName;
    private String fromCustomerAddress;
    private String fromCustomerPhoneNum;
    private double fromCustomerLat;
    private double fromCustomerLng;

    private boolean toCustomerExist;
    private String toCustomerId;
    private String toCustomerName;
    private String toCustomerAddress;
    private String toCustomerPhoneNum;
    private double toCustomerLat;
    private double toCustomerLng;

    private String packageName;
    private int weight;
    private String postPackageTypeId;
    private String description;

}

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

    public void print() {
        System.out.print(packageName + "|");
        System.out.print(weight + "|");
        System.out.print(postPackageTypeId + "|");
        System.out.print(description + "|");
        System.out.print(fromCustomerExist + "|");
        System.out.print(fromCustomerId + "|");
        System.out.print(fromCustomerName + "|");
        System.out.print(fromCustomerAddress + "|");
        System.out.print(fromCustomerPhoneNum + "|");
        System.out.print(fromCustomerLat + "|");
        System.out.print(fromCustomerLng + "|");
        System.out.print(toCustomerExist + "|");
        System.out.print(toCustomerId + "|");
        System.out.print(toCustomerName + "|");
        System.out.print(toCustomerAddress + "|");
        System.out.print(toCustomerPhoneNum + "|");
        System.out.print(toCustomerLat + "|");
        System.out.println(toCustomerLng);
    }
}

package com.hust.baseweb.applications.customer.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class CreateCustomerInputModel {
    private String customerName;
    private String address;
    private String latitude;
    private String longitude;

}

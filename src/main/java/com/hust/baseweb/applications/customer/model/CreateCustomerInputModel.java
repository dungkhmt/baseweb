package com.hust.baseweb.applications.customer.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateCustomerInputModel {

    private String customerCode;
    private String customerName;
    private String address;
    private Double latitude;
    private Double longitude;

}

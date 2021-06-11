package com.hust.baseweb.applications.specialpurpose.saleslogmongo.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateCustomerInputModel {

    private String customerName;
    private String address;
}

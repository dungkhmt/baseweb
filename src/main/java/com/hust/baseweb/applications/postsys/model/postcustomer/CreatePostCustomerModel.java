package com.hust.baseweb.applications.postsys.model.postcustomer;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreatePostCustomerModel {
    private String postCustomerName;
    private String phoneNum;
    private Double latitude;
    private Double longitude;
    private String address;
}

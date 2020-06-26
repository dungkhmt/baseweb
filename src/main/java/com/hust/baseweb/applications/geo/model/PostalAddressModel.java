package com.hust.baseweb.applications.geo.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostalAddressModel {

    private String address;
    private Double latitude;
    private Double longitude;

}

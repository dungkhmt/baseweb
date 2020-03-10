package com.hust.baseweb.applications.customer.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateDistributorInputModel {
	private String distributorCode;
    private String distributorName;
    private String address;
    private String latitude;
    private String longitude;
}

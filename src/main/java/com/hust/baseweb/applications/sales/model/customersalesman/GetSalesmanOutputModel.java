package com.hust.baseweb.applications.sales.model.customersalesman;

import com.hust.baseweb.applications.sales.entity.PartySalesman;
import com.hust.baseweb.entity.UserLogin;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetSalesmanOutputModel {
	private PartySalesman partySalesman;
	private String userLoginId;
	
	
}

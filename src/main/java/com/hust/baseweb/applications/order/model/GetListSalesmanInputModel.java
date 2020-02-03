package com.hust.baseweb.applications.order.model;

import lombok.Setter;
import lombok.Getter;

@Getter
@Setter
public class GetListSalesmanInputModel {
	private String statusId;

	public GetListSalesmanInputModel() {
		super();
		// TODO Auto-generated constructor stub
	}

	public GetListSalesmanInputModel(String statusId) {
		super();
		this.statusId = statusId;
	}
	
}

package com.hust.baseweb.applications.order.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class GetTotalRevenueOutputModel {
	private GetTotalRevenueItemOutputModel[] revenues;

	public GetTotalRevenueOutputModel() {
		super();
		// TODO Auto-generated constructor stub
	}

	public GetTotalRevenueOutputModel(GetTotalRevenueItemOutputModel[] revenues) {
		super();
		this.revenues = revenues;
	}
	
}

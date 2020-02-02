package com.hust.baseweb.applications.order.model;

import java.math.BigDecimal;

import lombok.Setter;
import lombok.Getter;

@Getter
@Setter

public class GetTotalRevenueItemOutputModel {
	private String date;
	private BigDecimal revenue;
	public GetTotalRevenueItemOutputModel() {
		super();
		// TODO Auto-generated constructor stub
	}
	public GetTotalRevenueItemOutputModel(String date, BigDecimal revenue) {
		super();
		this.date = date;
		this.revenue = revenue;
	}
	
}

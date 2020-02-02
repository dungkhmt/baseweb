package com.hust.baseweb.applications.order.entity.report;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RPTDayRevenue {
	private String date;
	private BigDecimal revenue;
	private int numberOrders;
	public RPTDayRevenue(String date, BigDecimal revenue, int numberOrders) {
		super();
		this.date = date;
		this.revenue = revenue;
		this.numberOrders = numberOrders;
	}
	public RPTDayRevenue() {
		super();
		// TODO Auto-generated constructor stub
	}
	
}

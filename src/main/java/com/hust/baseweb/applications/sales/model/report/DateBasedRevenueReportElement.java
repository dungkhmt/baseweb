package com.hust.baseweb.applications.sales.model.report;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class DateBasedRevenueReportElement {
	private String date;
	private Double revenue;
	public DateBasedRevenueReportElement(String date, Double revenue) {
		super();
		this.date = date;
		this.revenue = revenue;
	}
	
}

package com.hust.baseweb.applications.order.entity.aggregation;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RevenueAggregateUntilDate {
	private String date;// KEY, format yyyy-MM-dd
	private double totalRevenue;// total revenue aggregate until this date
	private List<ACustomerRevenue> customerRevenue;
	private List<AProductRevenue> productRevenue;
}

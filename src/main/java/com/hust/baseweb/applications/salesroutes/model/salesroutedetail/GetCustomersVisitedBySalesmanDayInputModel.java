package com.hust.baseweb.applications.salesroutes.model.salesroutedetail;

import java.util.Date;
import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class GetCustomersVisitedBySalesmanDayInputModel {
	private UUID partySalesmanId;
	private String date;
}

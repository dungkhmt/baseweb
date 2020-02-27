package com.hust.baseweb.applications.salesroutes.service;

import java.util.Date;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.hust.baseweb.applications.customer.entity.PartyCustomer;

import java.util.List;
@Service
public interface SalesRouteDetailService {
	public int generateSalesRouteDetailOfSalesman(UUID partySalesmanId, UUID salesRoutePlanningPeriodId);
	public List<PartyCustomer> getCustomersVisitedSalesmanDay(UUID partySalesmanId, String date);
}

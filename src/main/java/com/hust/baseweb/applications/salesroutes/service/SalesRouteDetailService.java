package com.hust.baseweb.applications.salesroutes.service;

import java.util.UUID;

import org.springframework.stereotype.Service;

@Service
public interface SalesRouteDetailService {
	public int generateSalesRouteDetailOfSalesman(UUID partySalesmanId, UUID salesRoutePlanningPeriodId);
}

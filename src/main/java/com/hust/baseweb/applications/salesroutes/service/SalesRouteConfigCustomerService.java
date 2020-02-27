package com.hust.baseweb.applications.salesroutes.service;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.hust.baseweb.applications.salesroutes.entity.SalesRouteConfigCustomer;

@Service
public interface SalesRouteConfigCustomerService {
	public SalesRouteConfigCustomer save(UUID salesRouteConfigId, UUID partyCustomerId, UUID partySalesmanId, String startExecuteDate);
}

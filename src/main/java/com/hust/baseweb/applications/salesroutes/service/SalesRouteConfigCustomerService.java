package com.hust.baseweb.applications.salesroutes.service;

import com.hust.baseweb.applications.salesroutes.entity.SalesRouteConfigCustomer;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public interface SalesRouteConfigCustomerService {
    SalesRouteConfigCustomer save(UUID salesRouteConfigId, UUID partyCustomerId, UUID partySalesmanId, String startExecuteDate);
}

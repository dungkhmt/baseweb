package com.hust.baseweb.applications.salesroutes.service;

import com.hust.baseweb.applications.customer.entity.PartyCustomer;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface SalesRouteDetailService {
    public int generateSalesRouteDetailOfSalesman(UUID partySalesmanId, UUID salesRoutePlanningPeriodId);

    public List<PartyCustomer> getCustomersVisitedSalesmanDay(UUID partySalesmanId, String date);
}

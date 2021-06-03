package com.hust.baseweb.applications.salesroutes.service;

import com.hust.baseweb.applications.customer.entity.PartyRetailOutlet;
import com.hust.baseweb.applications.salesroutes.model.salesroutedetail.GetSalesRouteDetailOfPlanPeriodOM;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface SalesRouteDetailService {

    int generateSalesRouteDetailOfSalesman(UUID partySalesmanId, UUID salesRoutePlanningPeriodId);

    List<PartyRetailOutlet> getRetailOutletsVisitedSalesmanDay(UUID partySalesmanId, String date);

    List<GetSalesRouteDetailOfPlanPeriodOM> getSalesRouteDetailOfPlanPeriod(UUID salesRoutePlanningPeriodId);
}

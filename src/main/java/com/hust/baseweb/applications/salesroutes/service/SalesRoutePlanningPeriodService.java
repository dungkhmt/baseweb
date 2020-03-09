package com.hust.baseweb.applications.salesroutes.service;

import com.hust.baseweb.applications.salesroutes.entity.SalesRoutePlanningPeriod;
import org.springframework.stereotype.Service;

@Service
public interface SalesRoutePlanningPeriodService {
    SalesRoutePlanningPeriod save(String fromDate, String toDate, String description);
}

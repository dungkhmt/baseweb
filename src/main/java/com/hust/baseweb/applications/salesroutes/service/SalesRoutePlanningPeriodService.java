package com.hust.baseweb.applications.salesroutes.service;

import org.springframework.stereotype.Service;

import com.hust.baseweb.applications.salesroutes.entity.SalesRoutePlanningPeriod;

@Service
public interface SalesRoutePlanningPeriodService {
	SalesRoutePlanningPeriod save(String fromDate, String toDate, String description);
}

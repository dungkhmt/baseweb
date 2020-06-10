package com.hust.baseweb.applications.salesroutes.service;

import com.hust.baseweb.applications.salesroutes.entity.SalesRoutePlanningPeriod;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public interface SalesRoutePlanningPeriodService {

    SalesRoutePlanningPeriod save(String fromDate, String toDate, String description);

    List<SalesRoutePlanningPeriod> findAll();

    SalesRoutePlanningPeriod findById(UUID salesRoutePlanningPeriodId);

    SalesRoutePlanningPeriod getCurrentPlanPeriod(Date currentDate);
}

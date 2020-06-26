package com.hust.baseweb.applications.salesroutes.repo;

import com.hust.baseweb.applications.salesroutes.entity.SalesRoutePlanningPeriod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.UUID;

public interface SalesRoutePlanningPeriodRepo extends JpaRepository<SalesRoutePlanningPeriod, UUID> {

    @Query(value = "from SalesRoutePlanningPeriod srpp where ?1 between srpp.fromDate and srpp.toDate")
    SalesRoutePlanningPeriod getCurrentPlanPeriod(Date currentDate);
}

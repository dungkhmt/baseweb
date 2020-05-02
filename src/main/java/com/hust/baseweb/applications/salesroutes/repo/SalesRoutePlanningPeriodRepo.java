package com.hust.baseweb.applications.salesroutes.repo;

import com.hust.baseweb.applications.salesroutes.entity.SalesRoutePlanningPeriod;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SalesRoutePlanningPeriodRepo extends JpaRepository<SalesRoutePlanningPeriod, UUID> {

}

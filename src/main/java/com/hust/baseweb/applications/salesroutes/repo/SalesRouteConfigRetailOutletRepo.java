package com.hust.baseweb.applications.salesroutes.repo;

import com.hust.baseweb.applications.sales.entity.PartySalesman;
import com.hust.baseweb.applications.salesroutes.entity.SalesRouteConfigRetailOutlet;
import com.hust.baseweb.applications.salesroutes.entity.SalesRoutePlanningPeriod;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface SalesRouteConfigRetailOutletRepo extends JpaRepository<SalesRouteConfigRetailOutlet, UUID> {
    List<SalesRouteConfigRetailOutlet> findBySalesRoutePlanningPeriod(SalesRoutePlanningPeriod salesRoutePlanningPeriod);

}

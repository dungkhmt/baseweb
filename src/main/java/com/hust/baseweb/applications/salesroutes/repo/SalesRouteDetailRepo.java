package com.hust.baseweb.applications.salesroutes.repo;

import com.hust.baseweb.applications.sales.entity.PartySalesman;
import com.hust.baseweb.applications.salesroutes.entity.SalesRouteDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;


public interface SalesRouteDetailRepo extends JpaRepository<SalesRouteDetail, UUID> {
    List<SalesRouteDetail> findByPartySalesmanAndExecuteDate(PartySalesman partySalesman, String executeDate);
    void deleteByPartySalesman(PartySalesman partySalesman);
}

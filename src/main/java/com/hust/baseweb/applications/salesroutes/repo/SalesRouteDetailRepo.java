package com.hust.baseweb.applications.salesroutes.repo;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hust.baseweb.applications.sales.entity.PartySalesman;
import com.hust.baseweb.applications.salesroutes.entity.SalesRouteDetail;


public interface SalesRouteDetailRepo extends JpaRepository<SalesRouteDetail, UUID> {
	public List<SalesRouteDetail> findByPartySalesmanAndExecuteDate(PartySalesman partySalesman, String executeDate);
}

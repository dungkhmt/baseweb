package com.hust.baseweb.applications.salesroutes.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hust.baseweb.applications.sales.entity.PartySalesman;
import com.hust.baseweb.applications.salesroutes.entity.SalesRouteConfigCustomer;

import java.util.List;
import java.util.UUID;

public interface SalesRouteConfigCustomerRepo extends JpaRepository<SalesRouteConfigCustomer, UUID> {
	List<SalesRouteConfigCustomer> findByPartySalesman(PartySalesman  partySalesman);
}

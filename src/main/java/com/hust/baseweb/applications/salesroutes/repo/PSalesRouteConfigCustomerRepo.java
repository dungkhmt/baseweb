package com.hust.baseweb.applications.salesroutes.repo;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.hust.baseweb.applications.salesroutes.entity.SalesRouteConfigCustomer;

import java.util.UUID;
public interface PSalesRouteConfigCustomerRepo extends
		PagingAndSortingRepository<SalesRouteConfigCustomer, UUID> {

}

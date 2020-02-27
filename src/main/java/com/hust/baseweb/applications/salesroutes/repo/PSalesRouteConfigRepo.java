package com.hust.baseweb.applications.salesroutes.repo;

import java.util.UUID;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.hust.baseweb.applications.salesroutes.entity.SalesRouteConfig;


public interface PSalesRouteConfigRepo extends PagingAndSortingRepository<SalesRouteConfig, UUID> {
	public SalesRouteConfig findBySalesRouteConfigId(UUID salesRouteConfigId);
}

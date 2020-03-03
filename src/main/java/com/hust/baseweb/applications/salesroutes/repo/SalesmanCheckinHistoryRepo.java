package com.hust.baseweb.applications.salesroutes.repo;

import java.util.UUID;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.hust.baseweb.applications.salesroutes.entity.SalesmanCheckinHistory;

public interface SalesmanCheckinHistoryRepo extends
		PagingAndSortingRepository<SalesmanCheckinHistory, UUID> {
	
}

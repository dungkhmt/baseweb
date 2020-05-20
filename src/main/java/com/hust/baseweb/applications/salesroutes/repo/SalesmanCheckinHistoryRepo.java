package com.hust.baseweb.applications.salesroutes.repo;

import com.hust.baseweb.applications.salesroutes.entity.SalesmanCheckinHistory;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.UUID;

public interface SalesmanCheckinHistoryRepo extends
    PagingAndSortingRepository<SalesmanCheckinHistory, UUID> {

}

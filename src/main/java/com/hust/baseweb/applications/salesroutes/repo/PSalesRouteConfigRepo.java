package com.hust.baseweb.applications.salesroutes.repo;

import com.hust.baseweb.applications.salesroutes.entity.SalesRouteConfig;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.UUID;


public interface PSalesRouteConfigRepo extends PagingAndSortingRepository<SalesRouteConfig, UUID> {

    SalesRouteConfig findBySalesRouteConfigId(UUID salesRouteConfigId);
}

package com.hust.baseweb.applications.salesroutes.repo;

import com.hust.baseweb.applications.salesroutes.entity.SalesRouteConfigCustomer;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.UUID;

public interface PSalesRouteConfigCustomerRepo extends
        PagingAndSortingRepository<SalesRouteConfigCustomer, UUID> {

}

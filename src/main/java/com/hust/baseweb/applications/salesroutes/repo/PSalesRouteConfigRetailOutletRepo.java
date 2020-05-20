package com.hust.baseweb.applications.salesroutes.repo;

import com.hust.baseweb.applications.salesroutes.entity.SalesRouteConfigRetailOutlet;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.UUID;

public interface PSalesRouteConfigRetailOutletRepo extends
    PagingAndSortingRepository<SalesRouteConfigRetailOutlet, UUID> {

}

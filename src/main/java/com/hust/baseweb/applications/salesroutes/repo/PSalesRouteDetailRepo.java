package com.hust.baseweb.applications.salesroutes.repo;

import com.hust.baseweb.applications.salesroutes.entity.SalesRouteDetail;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.UUID;

public interface PSalesRouteDetailRepo extends PagingAndSortingRepository<SalesRouteDetail, UUID> {

}

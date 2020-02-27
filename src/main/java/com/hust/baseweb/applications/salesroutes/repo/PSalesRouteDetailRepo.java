package com.hust.baseweb.applications.salesroutes.repo;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.hust.baseweb.applications.salesroutes.entity.SalesRouteDetail;
import java.util.UUID;
public interface PSalesRouteDetailRepo extends PagingAndSortingRepository<SalesRouteDetail, UUID> {

}

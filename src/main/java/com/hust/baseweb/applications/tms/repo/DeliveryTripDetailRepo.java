package com.hust.baseweb.applications.tms.repo;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.hust.baseweb.applications.tms.entity.CompositeDeliveryTripDetailId;
import com.hust.baseweb.applications.tms.entity.DeliveryTripDetail;

public interface DeliveryTripDetailRepo extends
		PagingAndSortingRepository<DeliveryTripDetail, CompositeDeliveryTripDetailId> {

}

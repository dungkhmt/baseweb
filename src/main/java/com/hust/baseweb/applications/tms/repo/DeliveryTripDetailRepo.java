package com.hust.baseweb.applications.tms.repo;

import com.hust.baseweb.applications.tms.entity.CompositeDeliveryTripDetailId;
import com.hust.baseweb.applications.tms.entity.DeliveryTripDetail;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface DeliveryTripDetailRepo extends
        PagingAndSortingRepository<DeliveryTripDetail, CompositeDeliveryTripDetailId> {

}

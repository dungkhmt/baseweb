package com.hust.baseweb.applications.tms.repo;

import com.hust.baseweb.applications.tms.entity.DeliveryPlan;
import com.hust.baseweb.applications.tms.entity.DeliveryTrip;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.UUID;

public interface DeliveryTripRepo extends PagingAndSortingRepository<DeliveryTrip, UUID> {
    Page<DeliveryTrip> findAllByDeliveryPlan(DeliveryPlan deliveryPlan, Pageable pageable);

    List<DeliveryTrip> findAllByDeliveryPlan(DeliveryPlan deliveryPlan);
}

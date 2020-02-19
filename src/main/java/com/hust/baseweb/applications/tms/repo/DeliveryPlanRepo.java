package com.hust.baseweb.applications.tms.repo;

import com.hust.baseweb.applications.tms.entity.DeliveryPlan;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.UUID;

public interface DeliveryPlanRepo extends PagingAndSortingRepository<DeliveryPlan, UUID> {
    public DeliveryPlan findByDeliveryPlanId(UUID deliveryPlanId);
}

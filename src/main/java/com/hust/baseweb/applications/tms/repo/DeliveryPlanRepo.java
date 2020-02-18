package com.hust.baseweb.applications.tms.repo;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.hust.baseweb.applications.tms.entity.DeliveryPlan;

import java.util.UUID;

public interface DeliveryPlanRepo extends PagingAndSortingRepository<DeliveryPlan, UUID> {
	public DeliveryPlan findByDeliveryPlanId(UUID deliveryPlanId);
}

package com.hust.baseweb.applications.tms.repo;

import com.hust.baseweb.applications.tms.entity.ShipmentItemDeliveryPlan;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.UUID;

public interface ShipmentItemDeliveryPlanRepo extends PagingAndSortingRepository<ShipmentItemDeliveryPlan, String> {
    List<ShipmentItemDeliveryPlan> findAllByDeliveryPlanId(UUID deliveryPlanId);
}

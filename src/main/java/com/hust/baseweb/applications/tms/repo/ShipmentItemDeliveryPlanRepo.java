package com.hust.baseweb.applications.tms.repo;

import com.hust.baseweb.applications.tms.entity.CompositeShipmentItemDeliveryPlanId;
import com.hust.baseweb.applications.tms.entity.ShipmentItemDeliveryPlan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ShipmentItemDeliveryPlanRepo
    extends JpaRepository<ShipmentItemDeliveryPlan, CompositeShipmentItemDeliveryPlanId> {

    List<ShipmentItemDeliveryPlan> findAllByDeliveryPlanId(String deliveryPlanId);

    Page<ShipmentItemDeliveryPlan> findAllByDeliveryPlanId(
        String deliveryPlanId,
        Pageable pageable
    );

    ShipmentItemDeliveryPlan findAllByDeliveryPlanIdAndShipmentItemId(
        String deliveryPlanId,
        UUID shipmentItemId
    );
}

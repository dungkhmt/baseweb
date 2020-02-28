package com.hust.baseweb.applications.tms.service;

import com.hust.baseweb.applications.tms.entity.ShipmentItem;
import com.hust.baseweb.applications.tms.model.shipmentitem.CreateShipmentItemDeliveryPlanModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ShipmentItemService {
    Iterable<String> findAllByDeliveryPlanId(String deliveryPlanId);

    Page<ShipmentItem> findAll(Pageable pageable);

    String saveShipmentItemDeliveryPlan(CreateShipmentItemDeliveryPlanModel createShipmentItemDeliveryPlanModel);
}

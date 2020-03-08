package com.hust.baseweb.applications.tms.service;

import com.hust.baseweb.applications.tms.entity.ShipmentItem;
import com.hust.baseweb.applications.tms.model.shipmentitem.CreateShipmentItemDeliveryPlanModel;
import com.hust.baseweb.applications.tms.model.shipmentitem.DeleteShipmentItemDeliveryPlanModel;
import com.hust.baseweb.applications.tms.model.shipmentitem.ShipmentItemDeliveryPlanModel;
import com.hust.baseweb.applications.tms.model.shipmentitem.ShipmentItemModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ShipmentItemService {
    Page<ShipmentItemModel> findAllInDeliveryPlanId(String deliveryPlanId, Pageable pageable);

    List<ShipmentItemModel> findAllInDeliveryPlanId(String deliveryPlanId);

    List<ShipmentItemDeliveryPlanModel> findAllInDeliveryPlanIdNearestDeliveryTrip(String deliveryTripId);

    Page<ShipmentItemModel> findAllNotInDeliveryPlanId(String deliveryPlanId, Pageable pageable);

    Page<ShipmentItem> findAll(Pageable pageable);

    Iterable<ShipmentItem> findAll();

    String saveShipmentItemDeliveryPlan(CreateShipmentItemDeliveryPlanModel createShipmentItemDeliveryPlanModel);

    boolean deleteShipmentItemDeliveryPlan(DeleteShipmentItemDeliveryPlanModel deleteShipmentItemDeliveryPlanModel);
}

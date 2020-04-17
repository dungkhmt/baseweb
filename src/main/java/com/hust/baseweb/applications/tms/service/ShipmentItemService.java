package com.hust.baseweb.applications.tms.service;

import com.hust.baseweb.applications.tms.entity.ShipmentItem;
import com.hust.baseweb.applications.tms.model.ShipmentItemModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ShipmentItemService {
    Page<ShipmentItemModel> findAllInDeliveryPlanId(String deliveryPlanId, Pageable pageable);

    List<ShipmentItemModel> findAllInDeliveryPlanId(String deliveryPlanId);

    List<ShipmentItemModel.DeliveryPlan> findAllInDeliveryPlanIdNearestDeliveryTrip(String deliveryTripId);

    Page<ShipmentItemModel> findAllNotInDeliveryPlanId(String deliveryPlanId, Pageable pageable);

    List<ShipmentItemModel> findAllNotInDeliveryPlanId(String deliveryPlanId);

    Page<ShipmentItem> findAll(Pageable pageable);

    Iterable<ShipmentItem> findAll();

    String saveShipmentItemDeliveryPlan(com.hust.baseweb.applications.tms.model.ShipmentItemModel.CreateDeliveryPlan createDeliveryPlan);

    boolean deleteShipmentItemDeliveryPlan(ShipmentItemModel.DeleteDeliveryPlan deleteDeliveryPlan);

    List<ShipmentItemModel> findAllNotScheduled(String deliveryPlanId);

    ShipmentItemModel.Info getShipmentItemInfo(String shipmentItemId);
}

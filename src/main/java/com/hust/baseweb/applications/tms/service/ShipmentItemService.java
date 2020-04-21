package com.hust.baseweb.applications.tms.service;

import com.hust.baseweb.applications.tms.entity.ShipmentItem;
import com.hust.baseweb.applications.tms.model.ShipmentItemModel;
import com.hust.baseweb.entity.UserLogin;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ShipmentItemService {
    Page<ShipmentItemModel> findAllInDeliveryPlan(String deliveryPlanId, Pageable pageable);

    List<ShipmentItemModel> findAllInDeliveryPlan(String deliveryPlanId);

    List<ShipmentItemModel.DeliveryPlan> findAllInDeliveryPlanNearestDeliveryTrip(String deliveryTripId);

    Page<ShipmentItemModel> findAllNotInDeliveryPlan(String deliveryPlanId, Pageable pageable);

    List<ShipmentItemModel> findAllNotInDeliveryPlan(String deliveryPlanId);
    List<ShipmentItemModel> findAllByUserLoginNotInDeliveryPlan(UserLogin userLogin, String deliveryPlanId);


    Page<ShipmentItem> findAll(Pageable pageable);

    Iterable<ShipmentItem> findAll();

    Page<ShipmentItem> findAllByUserLogin(UserLogin userLogin, Pageable pageable);

    String saveShipmentItemDeliveryPlan(com.hust.baseweb.applications.tms.model.ShipmentItemModel.CreateDeliveryPlan createDeliveryPlan);

    boolean deleteShipmentItemDeliveryPlan(ShipmentItemModel.DeleteDeliveryPlan deleteDeliveryPlan);

    List<ShipmentItemModel> findAllNotScheduled(String deliveryPlanId);

    ShipmentItemModel.Info getShipmentItemInfo(String shipmentItemId);
}

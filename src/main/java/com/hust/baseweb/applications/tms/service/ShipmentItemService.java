package com.hust.baseweb.applications.tms.service;

import com.hust.baseweb.applications.tms.entity.ShipmentItem;
import com.hust.baseweb.applications.tms.model.ShipmentItemModel;
import com.hust.baseweb.entity.UserLogin;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ShipmentItemService {

    Page<ShipmentItemModel> findAllInDeliveryPlan(
        String deliveryPlanId,
        Pageable pageable,
        UserLogin userLogin
    );

    List<ShipmentItemModel> findAllInDeliveryPlan(String deliveryPlanId, UserLogin userLogin);

    List<ShipmentItemModel.DeliveryPlan> findAllInDeliveryPlanNearestDeliveryTrip(
        String deliveryTripId,
        UserLogin userLogin
    );

    Page<ShipmentItemModel> findAllNotInDeliveryPlan(
        String deliveryPlanId,
        Pageable pageable,
        UserLogin userLogin
    );

    List<ShipmentItemModel> findAllNotInDeliveryPlan(String deliveryPlanId, UserLogin userLogin);

    List<ShipmentItemModel> findAllByUserLoginNotInDeliveryPlan(UserLogin userLogin, String deliveryPlanId);

    Page<ShipmentItem> findAll(Pageable pageable, UserLogin userLogin);

    Page<ShipmentItemModel> findAllByUserLogin(UserLogin userLogin, Pageable pageable);

    String saveShipmentItemDeliveryPlan(
        ShipmentItemModel.CreateDeliveryPlan createDeliveryPlan,
        UserLogin userLogin
    );

    boolean deleteShipmentItemDeliveryPlan(ShipmentItemModel.DeleteDeliveryPlan deleteDeliveryPlan);

    List<ShipmentItemModel> findAllNotScheduled(String deliveryPlanId, UserLogin userLogin);

    ShipmentItemModel.Info getShipmentItemInfo(String shipmentItemId);
}

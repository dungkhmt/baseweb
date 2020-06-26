package com.hust.baseweb.applications.tms.service;

import com.hust.baseweb.applications.tms.entity.DeliveryPlan;
import com.hust.baseweb.applications.tms.model.DeliveryPlanModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public interface DeliveryPlanService {

    DeliveryPlan save(DeliveryPlanModel.Create input);

    DeliveryPlan save(DeliveryPlan deliveryPlan);

    Page<DeliveryPlan> findAll(Pageable pageable);

    DeliveryPlan findById(String deliveryPlanId);

    Double getTotalWeightShipmentItems(String deliveryPlanId);
}

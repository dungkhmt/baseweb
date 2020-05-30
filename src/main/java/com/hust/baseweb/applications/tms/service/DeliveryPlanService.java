package com.hust.baseweb.applications.tms.service;

import com.hust.baseweb.applications.tms.entity.DeliveryPlan;
import com.hust.baseweb.applications.tms.model.DeliveryPlanModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public interface DeliveryPlanService {

    DeliveryPlan save(DeliveryPlanModel.Create input);

    Page<DeliveryPlan> findAll(Pageable pageable);

    DeliveryPlan findById(UUID deliveryPlanId);

    Double getTotalWeightShipmentItems(UUID deliveryPlanId);
}

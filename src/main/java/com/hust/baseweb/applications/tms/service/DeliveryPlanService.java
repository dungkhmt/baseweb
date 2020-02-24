package com.hust.baseweb.applications.tms.service;

import com.hust.baseweb.applications.tms.entity.DeliveryPlan;
import com.hust.baseweb.applications.tms.model.createdeliveryplan.CreateDeliveryPlanInputModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public interface DeliveryPlanService {
    public DeliveryPlan save(CreateDeliveryPlanInputModel input);

    public Page<DeliveryPlan> findAll(Pageable pageable);

    public DeliveryPlan findById(UUID deliveryPlanId);
}

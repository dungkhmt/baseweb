package com.hust.baseweb.applications.tms.service;

import com.hust.baseweb.applications.tms.entity.DeliveryPlan;
import com.hust.baseweb.applications.tms.model.createdeliveryplan.CreateDeliveryPlanInputModel;
import org.springframework.stereotype.Service;

@Service
public interface DeliveryPlanService {
    public DeliveryPlan save(CreateDeliveryPlanInputModel input);
}

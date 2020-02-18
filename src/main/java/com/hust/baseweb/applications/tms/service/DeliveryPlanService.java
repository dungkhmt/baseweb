package com.hust.baseweb.applications.tms.service;

import org.springframework.stereotype.Service;

import com.hust.baseweb.applications.tms.entity.DeliveryPlan;
import com.hust.baseweb.applications.tms.model.createdeliveryplan.CreateDeliveryPlanInputModel;

@Service
public interface DeliveryPlanService {
	public DeliveryPlan save(CreateDeliveryPlanInputModel input);
}

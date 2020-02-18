package com.hust.baseweb.applications.tms.service;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hust.baseweb.applications.tms.entity.DeliveryPlan;
import com.hust.baseweb.applications.tms.model.createdeliveryplan.CreateDeliveryPlanInputModel;
import com.hust.baseweb.applications.tms.repo.DeliveryPlanRepo;

@Service
public class DeliveryPlanServiceImpl implements DeliveryPlanService {

	@Autowired
	private DeliveryPlanRepo deliveryPlanRepo;
	
	@Override
	public DeliveryPlan save(CreateDeliveryPlanInputModel input) {
		// TODO Auto-generated method stub
		DeliveryPlan deliveryPlan = new DeliveryPlan();
		deliveryPlan.setCreatedByUserLoginId(input.getCreatedByUserLoginId());
		deliveryPlan.setDescription(input.getDescription());
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-mm-dd HH:MM:SS");
		Date deliveryDate = null;
		try{
			deliveryDate = formatter.parse(input.getDeliveryDate());
		}catch(Exception ex){
			ex.printStackTrace();
		}
		deliveryPlan.setDeliveryDate(deliveryDate);
		deliveryPlan = deliveryPlanRepo.save(deliveryPlan);
		
		return deliveryPlan;
	}

}

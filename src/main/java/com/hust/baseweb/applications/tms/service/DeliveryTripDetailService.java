package com.hust.baseweb.applications.tms.service;

import org.springframework.stereotype.Service;

import com.hust.baseweb.applications.tms.entity.DeliveryTripDetail;
import com.hust.baseweb.applications.tms.model.createdeliverytrip.CreateDeliveryTripDetailInputModel;

@Service
public interface DeliveryTripDetailService {
	public DeliveryTripDetail save(CreateDeliveryTripDetailInputModel input);
}

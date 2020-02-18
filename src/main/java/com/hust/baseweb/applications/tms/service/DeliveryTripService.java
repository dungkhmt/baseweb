package com.hust.baseweb.applications.tms.service;

import org.springframework.stereotype.Service;

import com.hust.baseweb.applications.tms.entity.DeliveryTrip;
import com.hust.baseweb.applications.tms.model.createdeliverytrip.CreateDeliveryTripInputModel;

@Service
public interface DeliveryTripService {
	public DeliveryTrip save(CreateDeliveryTripInputModel input);
}

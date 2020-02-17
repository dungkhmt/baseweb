package com.hust.baseweb.applications.tms.service;

import org.springframework.stereotype.Service;

import com.hust.baseweb.applications.tms.entity.Shipment;
import com.hust.baseweb.applications.tms.model.shipmentorder.CreateShipmentInputModel;


@Service
public interface ShipmentService {
	public Shipment save(CreateShipmentInputModel input);
}

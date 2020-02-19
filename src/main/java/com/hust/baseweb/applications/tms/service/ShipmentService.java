package com.hust.baseweb.applications.tms.service;

import com.hust.baseweb.applications.tms.entity.Shipment;
import com.hust.baseweb.applications.tms.model.shipmentorder.CreateShipmentInputModel;
import org.springframework.stereotype.Service;


@Service
public interface ShipmentService {
    public Shipment save(CreateShipmentInputModel input);
}

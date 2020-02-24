package com.hust.baseweb.applications.tms.service;

import com.hust.baseweb.applications.tms.entity.Shipment;
import com.hust.baseweb.applications.tms.model.shipmentorder.CreateShipmentInputModel;
import com.hust.baseweb.applications.tms.model.shipmentorder.ShipmentModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
public interface ShipmentService {
    public Shipment save(CreateShipmentInputModel input);

    Page<ShipmentModel> findAll(Pageable pageable);
}

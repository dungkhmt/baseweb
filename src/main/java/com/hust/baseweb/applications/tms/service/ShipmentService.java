package com.hust.baseweb.applications.tms.service;

import com.hust.baseweb.applications.tms.entity.Shipment;
import com.hust.baseweb.applications.tms.model.ShipmentItemModel;
import com.hust.baseweb.applications.tms.model.ShipmentModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
public interface ShipmentService {
    Shipment save(ShipmentModel.CreateShipmentInputModel input);

    Shipment save(ShipmentItemModel.Create input);

    Page<ShipmentModel> findAll(Pageable pageable);
}

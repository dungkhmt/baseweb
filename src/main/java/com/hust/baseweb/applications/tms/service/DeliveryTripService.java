package com.hust.baseweb.applications.tms.service;

import com.hust.baseweb.applications.tms.entity.DeliveryTrip;
import com.hust.baseweb.applications.tms.model.createdeliverytrip.CreateDeliveryTripInputModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public interface DeliveryTripService {
    public DeliveryTrip save(CreateDeliveryTripInputModel input);

    Page<DeliveryTrip> findAll(Pageable pageable);

    DeliveryTrip findById(UUID deliveryTripId);
}

package com.hust.baseweb.applications.tms.service;

import com.hust.baseweb.applications.tms.entity.DeliveryTrip;
import com.hust.baseweb.applications.tms.model.createdeliverytrip.CreateDeliveryTripInputModel;
import org.springframework.stereotype.Service;

@Service
public interface DeliveryTripService {
    public DeliveryTrip save(CreateDeliveryTripInputModel input);
}

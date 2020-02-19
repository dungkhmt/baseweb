package com.hust.baseweb.applications.tms.service;

import com.hust.baseweb.applications.tms.entity.DeliveryTripDetail;
import com.hust.baseweb.applications.tms.model.createdeliverytrip.CreateDeliveryTripDetailInputModel;
import org.springframework.stereotype.Service;

@Service
public interface DeliveryTripDetailService {
    public DeliveryTripDetail save(CreateDeliveryTripDetailInputModel input);
}

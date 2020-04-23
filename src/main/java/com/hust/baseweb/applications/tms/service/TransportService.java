package com.hust.baseweb.applications.tms.service;

import com.hust.baseweb.applications.tms.entity.DeliveryTrip;

/**
 * @author Hien Hoang (hienhoang2702@gmail.com)
 */
public interface TransportService {

    void updateTransport(DeliveryTrip... deliveryTrips);
}

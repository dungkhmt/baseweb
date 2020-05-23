package com.hust.baseweb.applications.adminmaintenance.service.tms;

import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public interface DeliveryTripMaintenanceService {
    int deleteDeliveryTrip(UUID deliveryTripId);

    long deleteAllDeliveryTrip();
}

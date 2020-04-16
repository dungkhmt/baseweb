package com.hust.baseweb.applications.adminmaintenance.service.tms;

import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public interface DeliveryTripMaintenanceService {
    public int deleteDeliveryTrip(UUID deliveryTripId);
    public long deleteAllDeliveryTrip();
}

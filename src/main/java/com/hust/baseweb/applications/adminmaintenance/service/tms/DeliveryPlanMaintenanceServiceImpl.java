package com.hust.baseweb.applications.adminmaintenance.service.tms;

import com.hust.baseweb.applications.tms.repo.*;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class DeliveryPlanMaintenanceServiceImpl implements DeliveryPlanMaintenanceService {

    private DeliveryPlanRepo deliveryPlanRepo;
    private ShipmentItemDeliveryPlanRepo shipmentItemDeliveryPlanRepo;
    private DeliveryTripDetailRepo deliveryTripDetailRepo;
    private DeliveryTripRepo deliveryTripRepo;
    private VehicleDeliveryPlanRepo vehicleDeliveryPlanRepo;

    @Override
    public long deleteAllDeliveryPlan() {

        long cnt = deliveryPlanRepo.count();
        shipmentItemDeliveryPlanRepo.deleteAll();
        deliveryTripDetailRepo.deleteAll();
        deliveryTripRepo.deleteAll();
        vehicleDeliveryPlanRepo.deleteAll();
        deliveryPlanRepo.deleteAll();

        return cnt;
    }
}

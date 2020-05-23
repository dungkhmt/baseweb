package com.hust.baseweb.applications.adminmaintenance.service.tms;

import com.hust.baseweb.applications.tms.repo.DeliveryTripDetailRepo;
import com.hust.baseweb.applications.tms.repo.DeliveryTripRepo;
import com.hust.baseweb.applications.tms.repo.status.DeliveryTripStatusRepo;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class DeliveryTripMaintenanceServiceImpl implements DeliveryTripMaintenanceService {
    private DeliveryTripDetailRepo deliveryTripDetailRepo;
    private DeliveryTripStatusRepo deliveryTripStatusRepo;
    private DeliveryTripRepo deliveryTripRepo;

    @Override
    public int deleteDeliveryTrip(UUID deliveryTripId) {
        // TODO: PQD
        return 0;
    }

    @Override
    public long deleteAllDeliveryTrip() {
        long cnt = deliveryTripRepo.count();
        deliveryTripDetailRepo.deleteAll();
        deliveryTripStatusRepo.deleteAll();
        deliveryTripRepo.deleteAll();
        return cnt;
    }
}

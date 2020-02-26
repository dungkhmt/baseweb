package com.hust.baseweb.applications.tms.service;

import com.hust.baseweb.applications.tms.entity.DeliveryTrip;
import com.hust.baseweb.applications.tms.entity.Vehicle;
import com.hust.baseweb.applications.tms.model.createdeliverytrip.CreateDeliveryTripInputModel;
import com.hust.baseweb.applications.tms.repo.DeliveryPlanRepo;
import com.hust.baseweb.applications.tms.repo.DeliveryTripRepo;
import com.hust.baseweb.applications.tms.repo.VehicleRepo;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class DeliveryTripServiceImpl implements DeliveryTripService {

    private DeliveryTripRepo deliveryTripRepo;
    private DeliveryPlanRepo deliveryPlanRepo;
    private VehicleRepo vehicleRepo;

    @Override
    @Transactional
    public DeliveryTrip save(CreateDeliveryTripInputModel input) {
        DeliveryTrip deliveryTrip = new DeliveryTrip();
        deliveryTrip.setDeliveryPlan(deliveryPlanRepo.findByDeliveryPlanId(input.getDeliveryPlanId()));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date executeDate = null;
        try {
            executeDate = formatter.parse(input.getExecuteDate());
        } catch (Exception e) {
            e.printStackTrace();
        }
        deliveryTrip.setExecuteDate(executeDate);

        Vehicle vehicle = vehicleRepo.findById(input.getVehicleId()).orElse(new Vehicle(input.getVehicleId()));
        vehicleRepo.save(vehicle);

        deliveryTrip.setVehicle(vehicle);

        deliveryTrip = deliveryTripRepo.save(deliveryTrip);
        return deliveryTrip;
    }

    @Override
    public Page<DeliveryTrip> findAll(Pageable pageable) {
        return deliveryTripRepo.findAll(pageable);
    }

    @Override
    public DeliveryTrip findById(UUID deliveryTripId) {
        return deliveryTripRepo.findById(deliveryTripId).orElseThrow(NoSuchElementException::new);
    }
}

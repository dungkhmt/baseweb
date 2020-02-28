package com.hust.baseweb.applications.tms.service;

import com.hust.baseweb.applications.tms.entity.Vehicle;
import com.hust.baseweb.applications.tms.model.vehicle.CreateVehicleDeliveryPlanModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface VehicleService {
    Page<Vehicle> findAll(Pageable pageable);

    Iterable<Vehicle> findAll();

    void save(Vehicle vehicle);

    void saveAll(List<Vehicle> vehicles);

    List<String> findAllByDeliveryPlanId(String deliveryPlan);

    String saveVehicleDeliveryPlan(CreateVehicleDeliveryPlanModel createVehicleDeliveryPlanModel);
}

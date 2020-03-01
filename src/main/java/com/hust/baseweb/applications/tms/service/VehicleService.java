package com.hust.baseweb.applications.tms.service;

import com.hust.baseweb.applications.tms.entity.Vehicle;
import com.hust.baseweb.applications.tms.model.vehicle.CreateVehicleDeliveryPlanModel;
import com.hust.baseweb.applications.tms.model.vehicle.DeleteVehicleDeliveryPlanModel;
import com.hust.baseweb.applications.tms.model.vehicle.VehicleModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface VehicleService {
    Page<Vehicle> findAll(Pageable pageable);

    Iterable<Vehicle> findAll();

    void save(Vehicle vehicle);

    void saveAll(List<Vehicle> vehicles);

    Page<VehicleModel> findAllInDeliveryPlanId(String deliveryPlanId, Pageable pageable);

    Page<VehicleModel> findAllNotInDeliveryPlanId(String deliveryPlanId, Pageable pageable);

    String saveVehicleDeliveryPlan(CreateVehicleDeliveryPlanModel createVehicleDeliveryPlanModel);

    boolean deleteVehicleDeliveryPlan(DeleteVehicleDeliveryPlanModel deleteVehicleDeliveryPlanModel);
}

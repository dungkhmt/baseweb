package com.hust.baseweb.applications.tms.service;

import com.hust.baseweb.applications.tms.entity.Vehicle;
import com.hust.baseweb.applications.tms.entity.VehicleMaintenanceHistory;
import com.hust.baseweb.applications.tms.model.LocationModel;
import com.hust.baseweb.applications.tms.model.VehicleModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface VehicleService {

    Page<Vehicle> findAll(Pageable pageable);

    List<Vehicle> findAll();

    void saveAll(List<Vehicle> vehicles);

    void saveAllMaintenanceHistory(List<VehicleMaintenanceHistory> vehicleMaintenanceHistories);

    Page<VehicleModel> findAllInDeliveryPlan(String deliveryPlanId, Pageable pageable);

    List<VehicleModel> findAllInDeliveryPlan(String deliveryPlanId);

    Page<VehicleModel> findAllNotInDeliveryPlan(String deliveryPlanId, Pageable pageable);

    List<VehicleModel> findAllNotInDeliveryPlan(String deliveryPlanId);

    List<VehicleModel> findAllNotInDeliveryTrips(String deliveryPlanId);

    String saveVehicleDeliveryPlan(VehicleModel.CreateDeliveryPlan createDeliveryPlan);

    boolean deleteVehicleDeliveryPlan(VehicleModel.DeleteDeliveryPlan deleteDeliveryPlan);

    List<Vehicle> save(
        List<VehicleModel.Create> vehicleModels,
        List<VehicleModel.CreateLocationPriority> vehicleLocationPriorities,
        List<LocationModel.Create> shipToModels
    );
}

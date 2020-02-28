package com.hust.baseweb.applications.tms.service;

import com.hust.baseweb.applications.tms.entity.Vehicle;
import com.hust.baseweb.applications.tms.entity.VehicleDeliveryPlan;
import com.hust.baseweb.applications.tms.model.vehicle.CreateVehicleDeliveryPlanModel;
import com.hust.baseweb.applications.tms.repo.VehicleDeliveryPlanRepo;
import com.hust.baseweb.applications.tms.repo.VehicleRepo;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class VehicleServiceImpl implements VehicleService {

    private VehicleRepo vehicleRepo;
    private VehicleDeliveryPlanRepo vehicleDeliveryPlanRepo;

    @Override
    public Page<Vehicle> findAll(Pageable pageable) {
        return vehicleRepo.findAll(pageable);
    }

    @Override
    public Iterable<Vehicle> findAll() {
        return vehicleRepo.findAll();
    }

    @Override
    public void save(Vehicle vehicle) {
        vehicleRepo.save(vehicle);
    }

    @Override
    public void saveAll(List<Vehicle> vehicles) {
        vehicleRepo.saveAll(vehicles);
    }

    @Override
    public List<String> findAllByDeliveryPlanId(String deliveryPlanId) {
        return vehicleDeliveryPlanRepo.findAllByDeliveryPlanId(UUID.fromString(deliveryPlanId))
                .stream().map(vehicleDeliveryPlan -> vehicleDeliveryPlan.getDeliveryPlanId().toString())
                .distinct()
                .collect(Collectors.toList());
    }

    @Override
    public String saveVehicleDeliveryPlan(CreateVehicleDeliveryPlanModel createVehicleDeliveryPlanModel) {
        List<VehicleDeliveryPlan> vehicleDeliveryPlans = new ArrayList<>();
        for (String vehicleId : createVehicleDeliveryPlanModel.getVehicleIds()) {
            vehicleDeliveryPlans.add(new VehicleDeliveryPlan(
                    UUID.fromString(vehicleId),
                    UUID.fromString(createVehicleDeliveryPlanModel.getDeliveryPlanId())
            ));
        }
        vehicleDeliveryPlanRepo.saveAll(vehicleDeliveryPlans);
        return createVehicleDeliveryPlanModel.getDeliveryPlanId();
    }
}

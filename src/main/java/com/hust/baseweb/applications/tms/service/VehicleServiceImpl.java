package com.hust.baseweb.applications.tms.service;

import com.hust.baseweb.applications.tms.entity.Vehicle;
import com.hust.baseweb.applications.tms.entity.VehicleDeliveryPlan;
import com.hust.baseweb.applications.tms.model.vehicle.CreateVehicleDeliveryPlanModel;
import com.hust.baseweb.applications.tms.model.vehicle.DeleteVehicleDeliveryPlanModel;
import com.hust.baseweb.applications.tms.model.vehicle.VehicleModel;
import com.hust.baseweb.applications.tms.repo.VehicleDeliveryPlanRepo;
import com.hust.baseweb.applications.tms.repo.VehicleRepo;
import com.hust.baseweb.utils.PageUtils;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
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
    public Page<VehicleModel> findAllInDeliveryPlanId(String deliveryPlanId, Pageable pageable) {
        Page<VehicleDeliveryPlan> deliveryPlanPage
                = vehicleDeliveryPlanRepo.findAllByDeliveryPlanId(UUID.fromString(deliveryPlanId), pageable);

        return new PageImpl<>(vehicleRepo.findAllByVehicleIdIn(
                deliveryPlanPage.map(VehicleDeliveryPlan::getVehicleId).getContent())
                .stream().map(Vehicle::toVehicleModel)
                .collect(Collectors.toList()),
                pageable,
                deliveryPlanPage.getTotalElements()
        );
    }

    @Override
    public Page<VehicleModel> findAllNotInDeliveryPlanId(String deliveryPlanId, Pageable pageable) {
        Set<String> vehicleInDeliveryPlans = vehicleDeliveryPlanRepo.findAllByDeliveryPlanId(UUID.fromString(deliveryPlanId))
                .stream().map(vehicleDeliveryPlan -> vehicleDeliveryPlan.getVehicleId().toString())
                .collect(Collectors.toSet());
        List<Vehicle> allVehicles = new ArrayList<>();
        vehicleRepo.findAll().forEach(allVehicles::add);
        List<VehicleModel> vehicleModels = allVehicles.stream()
                .filter(vehicle -> !vehicleInDeliveryPlans.contains(vehicle.getVehicleId().toString()))
                .map(Vehicle::toVehicleModel)
                .collect(Collectors.toList());
        return PageUtils.getPage(vehicleModels, pageable);
    }

    @Override
    public String saveVehicleDeliveryPlan(CreateVehicleDeliveryPlanModel createVehicleDeliveryPlanModel) {
        List<VehicleDeliveryPlan> vehicleDeliveryPlans = new ArrayList<>();
        for (String vehicleId : createVehicleDeliveryPlanModel.getVehicleIds()) {
            vehicleDeliveryPlans.add(new VehicleDeliveryPlan(
                    vehicleId,
                    UUID.fromString(createVehicleDeliveryPlanModel.getDeliveryPlanId())
            ));
        }
        vehicleDeliveryPlanRepo.saveAll(vehicleDeliveryPlans);
        return createVehicleDeliveryPlanModel.getDeliveryPlanId();
    }

    @Override
    public boolean deleteVehicleDeliveryPlan(DeleteVehicleDeliveryPlanModel deleteVehicleDeliveryPlanModel) {
        VehicleDeliveryPlan vehicleDeliveryPlan = vehicleDeliveryPlanRepo.findByDeliveryPlanIdAndVehicleId(
                UUID.fromString(deleteVehicleDeliveryPlanModel.getDeliveryPlanId()),
                deleteVehicleDeliveryPlanModel.getVehicleId()
        );
        if (vehicleDeliveryPlan != null) {
            vehicleDeliveryPlanRepo.delete(vehicleDeliveryPlan);
            return true;
        }
        return false;
    }
}

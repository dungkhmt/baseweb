package com.hust.baseweb.applications.tms.service;

import com.hust.baseweb.applications.tms.entity.Vehicle;
import com.hust.baseweb.applications.tms.entity.VehicleDeliveryPlan;
import com.hust.baseweb.applications.tms.entity.VehicleMaintenanceHistory;
import com.hust.baseweb.applications.tms.model.createvehicle.CreateVehicleModel;
import com.hust.baseweb.applications.tms.model.vehicle.CreateVehicleDeliveryPlanModel;
import com.hust.baseweb.applications.tms.model.vehicle.DeleteVehicleDeliveryPlanModel;
import com.hust.baseweb.applications.tms.model.vehicle.VehicleModel;
import com.hust.baseweb.applications.tms.repo.VehicleDeliveryPlanRepo;
import com.hust.baseweb.applications.tms.repo.VehicleMaintenanceHistoryRepo;
import com.hust.baseweb.applications.tms.repo.VehicleRepo;
import com.hust.baseweb.utils.PageUtils;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
@Log4j2
public class VehicleServiceImpl implements VehicleService {

    private VehicleRepo vehicleRepo;
    private VehicleDeliveryPlanRepo vehicleDeliveryPlanRepo;
    private VehicleMaintenanceHistoryRepo vehicleMaintenanceHistoryRepo;

    @Override
    public Page<Vehicle> findAll(Pageable pageable) {
        return vehicleMaintenanceHistoryRepo.findAllByThruDateIsNull(pageable)
                .map(VehicleMaintenanceHistory::getVehicle);
    }

    @Override
    public List<Vehicle> findAll() {
        return vehicleMaintenanceHistoryRepo.findAllByThruDateIsNull().stream()
                .map(VehicleMaintenanceHistory::getVehicle).collect(Collectors.toList());
    }

    @Override
    public void saveAll(List<Vehicle> vehicles) {
        Set<String> vehicleIdSet = vehicleMaintenanceHistoryRepo
                .findAllByThruDateIsNullAndVehicleIn(vehicles)
                .stream().map(vehicleMaintenanceHistory -> vehicleMaintenanceHistory.getVehicle().getVehicleId())
                .collect(Collectors.toSet());
        vehicles = vehicles.stream()
                .filter(vehicle -> !vehicleIdSet.contains(vehicle.getVehicleId()))
                .collect(Collectors.toList());
        vehicleRepo.saveAll(vehicles);
        vehicleMaintenanceHistoryRepo.saveAll(vehicles.stream()
                .map(Vehicle::createVehicleMaintenanceHistory).collect(Collectors.toList()));
    }

    @Override
    public void saveAllMaintenanceHistory(List<VehicleMaintenanceHistory> vehicleMaintenanceHistories) {
        vehicleMaintenanceHistoryRepo.saveAll(vehicleMaintenanceHistories);
    }

    // TODO:
    @Override
    public Page<VehicleModel> findAllInDeliveryPlanId(String deliveryPlanId, Pageable pageable) {
        List<VehicleDeliveryPlan> vehicleDeliveryPlans
                = vehicleDeliveryPlanRepo.findAllByDeliveryPlanId(UUID.fromString(deliveryPlanId));

        List<VehicleModel> vehicleModels = vehicleRepo.findAllByVehicleIdIn(
                vehicleDeliveryPlans.stream().map(VehicleDeliveryPlan::getVehicleId).collect(Collectors.toList()))
                .stream().map(Vehicle::toVehicleModel)
                .collect(Collectors.toList());

        return PageUtils.getPage(vehicleModels, pageable);
    }

    // TODO:
    @Override
    public Page<VehicleModel> findAllNotInDeliveryPlanId(String deliveryPlanId, Pageable pageable) {
        Set<String> vehicleInDeliveryPlans = vehicleDeliveryPlanRepo.findAllByDeliveryPlanId(UUID.fromString(deliveryPlanId))
                .stream().map(VehicleDeliveryPlan::getVehicleId)
                .collect(Collectors.toSet());
        List<VehicleMaintenanceHistory> vehicleMaintenanceHistories = vehicleMaintenanceHistoryRepo.findAllByThruDateIsNull();

        List<Vehicle> vehicles = vehicleRepo.findAllByVehicleIdIn(vehicleMaintenanceHistories.stream()
                .map(vehicleMaintenanceHistory -> vehicleMaintenanceHistory.getVehicle().getVehicleId())
                .distinct().collect(Collectors.toList()));

        List<VehicleModel> vehicleModels = vehicles.stream()
                .filter(vehicle -> !vehicleInDeliveryPlans.contains(vehicle.getVehicleId()))
                .map(Vehicle::toVehicleModel)
                .collect(Collectors.toList());
        return PageUtils.getPage(vehicleModels, pageable);
    }

    @Override
    public String saveVehicleDeliveryPlan(CreateVehicleDeliveryPlanModel createVehicleDeliveryPlanModel) {
        List<VehicleDeliveryPlan> vehicleDeliveryPlans = new ArrayList<>();
        Set<String> vehicleIdSet = vehicleMaintenanceHistoryRepo.findAllByThruDateIsNullAndVehicleIn(
                vehicleRepo.findAllByVehicleIdIn(createVehicleDeliveryPlanModel.getVehicleIds().stream().distinct().collect(Collectors.toList())))
                .stream().map(vehicleMaintenanceHistory -> vehicleMaintenanceHistory.getVehicle().getVehicleId()).collect(Collectors.toSet());

        for (String vehicleId : createVehicleDeliveryPlanModel.getVehicleIds()) {
            if (!vehicleIdSet.contains(vehicleId)) {
                continue;
            }
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

	@Override
	@Transactional
	public List<Vehicle> save(List<CreateVehicleModel> vehicleModels) {
		// TODO Auto-generated method stub
		List<Vehicle> listVehicles = new ArrayList<>();
		for(int i = 0; i < vehicleModels.size(); i++){
			CreateVehicleModel vm = vehicleModels.get(i);
			Vehicle vehicle = vehicleRepo.findByVehicleId(vm.getVehicleId());
			if(vehicle == null){
				vehicle = new Vehicle();
				vehicle.setVehicleId(vm.getVehicleId());
				vehicle.setCapacity(vm.getCapacity());
				vehicle.setDescription(vm.getDescription());
				vehicle.setHeight(vm.getHeight());
				vehicle.setLength(vm.getLength());
				vehicle.setWidth(vm.getWidth());
				vehicle.setPallet(vm.getPallet());
				vehicle = vehicleRepo.save(vehicle);
			
				VehicleMaintenanceHistory vmh = vehicle.createVehicleMaintenanceHistory();
				vmh = vehicleMaintenanceHistoryRepo.save(vmh);
				
				listVehicles.add(vehicle);
			}else{
				
			}
			log.info("save vehicles, finished " + i + "/" + vehicleModels.size());
			
		}
		return listVehicles;
	}
}

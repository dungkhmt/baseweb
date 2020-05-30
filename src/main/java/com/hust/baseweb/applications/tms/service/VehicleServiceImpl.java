package com.hust.baseweb.applications.tms.service;

import com.hust.baseweb.applications.geo.entity.PostalAddress;
import com.hust.baseweb.applications.geo.repo.PostalAddressRepo;
import com.hust.baseweb.applications.tms.entity.*;
import com.hust.baseweb.applications.tms.model.LocationModel;
import com.hust.baseweb.applications.tms.model.VehicleModel;
import com.hust.baseweb.applications.tms.repo.*;
import com.hust.baseweb.utils.PageUtils;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
@Log4j2
public class VehicleServiceImpl implements VehicleService {

    private VehicleRepo vehicleRepo;
    private PostalAddressRepo postalAddressRepo;
    private VehicleDeliveryPlanRepo vehicleDeliveryPlanRepo;
    private VehicleMaintenanceHistoryRepo vehicleMaintenanceHistoryRepo;
    private VehicleLocationPriorityRepo vehicleLocationPriorityRepo;

    private DeliveryTripRepo deliveryTripRepo;
    private DeliveryPlanRepo deliveryPlanRepo;

    @Override
    public Page<Vehicle> findAll(Pageable pageable) {
        return vehicleMaintenanceHistoryRepo
            .findAllByThruDateIsNull(pageable)
            .map(VehicleMaintenanceHistory::getVehicle);
    }

    @Override
    public List<Vehicle> findAll() {
        return vehicleMaintenanceHistoryRepo
            .findAllByThruDateIsNull()
            .stream()
            .map(VehicleMaintenanceHistory::getVehicle)
            .collect(Collectors.toList());
    }

    @Override
    public void saveAll(List<Vehicle> vehicles) {
        Set<String> vehicleIdSet = vehicleMaintenanceHistoryRepo
            .findAllByThruDateIsNullAndVehicleIn(vehicles)
            .stream()
            .map(vehicleMaintenanceHistory -> vehicleMaintenanceHistory.getVehicle().getVehicleId())
            .collect(Collectors.toSet());
        vehicles = vehicles
            .stream()
            .filter(vehicle -> !vehicleIdSet.contains(vehicle.getVehicleId()))
            .collect(Collectors.toList());
        vehicleRepo.saveAll(vehicles);
        vehicleMaintenanceHistoryRepo.saveAll(vehicles
                                                  .stream()
                                                  .map(Vehicle::createVehicleMaintenanceHistory)
                                                  .collect(Collectors.toList()));
    }

    @Override
    public void saveAllMaintenanceHistory(List<VehicleMaintenanceHistory> vehicleMaintenanceHistories) {
        vehicleMaintenanceHistoryRepo.saveAll(vehicleMaintenanceHistories);
    }

    // TODO:
    @Override
    public Page<VehicleModel> findAllInDeliveryPlan(String deliveryPlanId, Pageable pageable) {
        List<VehicleDeliveryPlan> vehicleDeliveryPlans
            = vehicleDeliveryPlanRepo.findAllByDeliveryPlanId(UUID.fromString(deliveryPlanId));

        List<VehicleModel> vehicleModels = vehicleRepo.findAllByVehicleIdIn(
            vehicleDeliveryPlans
                .stream()
                .map(VehicleDeliveryPlan::getVehicleId)
                .distinct()
                .collect(Collectors.toList())).stream().map(Vehicle::toVehicleModel).collect(Collectors.toList());

        return PageUtils.getPage(vehicleModels, pageable);
    }

    @Override
    public List<VehicleModel> findAllInDeliveryPlan(String deliveryPlanId) {
        List<VehicleDeliveryPlan> vehicleDeliveryPlans = vehicleDeliveryPlanRepo.findAllByDeliveryPlanId(UUID.fromString(
            deliveryPlanId));
        return vehicleRepo.findAllByVehicleIdIn(
            vehicleDeliveryPlans
                .stream()
                .map(VehicleDeliveryPlan::getVehicleId)
                .distinct()
                .collect(Collectors.toList())).stream().map(Vehicle::toVehicleModel).collect(Collectors.toList());
    }

    // TODO:
    @Override
    public Page<VehicleModel> findAllNotInDeliveryPlan(String deliveryPlanId, Pageable pageable) {
        return PageUtils.getPage(findAllNotInDeliveryPlan(deliveryPlanId), pageable);
    }

    @Override
    public List<VehicleModel> findAllNotInDeliveryPlan(String deliveryPlanId) {
        Set<String> vehicleInDeliveryPlans = vehicleDeliveryPlanRepo
            .findAllByDeliveryPlanId(
                UUID.fromString(deliveryPlanId))
            .stream()
            .map(VehicleDeliveryPlan::getVehicleId)
            .collect(Collectors.toSet());
        List<VehicleMaintenanceHistory> vehicleMaintenanceHistories = vehicleMaintenanceHistoryRepo.findAllByThruDateIsNull();

        List<Vehicle> vehicles = vehicleRepo.findAllByVehicleIdIn(vehicleMaintenanceHistories
                                                                      .stream()
                                                                      .map(vehicleMaintenanceHistory -> vehicleMaintenanceHistory
                                                                          .getVehicle()
                                                                          .getVehicleId())
                                                                      .distinct()
                                                                      .collect(Collectors.toList()));

        return vehicles
            .stream()
            .filter(vehicle -> !vehicleInDeliveryPlans.contains(vehicle.getVehicleId()))
            .map(Vehicle::toVehicleModel)
            .collect(Collectors.toList());
    }

    @Override
    public List<VehicleModel> findAllNotInDeliveryTrips(String deliveryPlanId) {
        DeliveryPlan deliveryPlan = deliveryPlanRepo
            .findById(UUID.fromString(deliveryPlanId))
            .orElseThrow(NoSuchElementException::new);

        List<VehicleDeliveryPlan> vehicleDeliveryPlans = vehicleDeliveryPlanRepo.findAllByDeliveryPlanId(UUID.fromString(
            deliveryPlanId));
        List<DeliveryTrip> deliveryTrips = deliveryTripRepo.findAllByDeliveryPlan(deliveryPlan);
        Map<String, List<DeliveryTrip>> vehicleIdToDeliveryTrips = deliveryTrips
            .stream()
            .collect(Collectors.groupingBy(deliveryTrip -> deliveryTrip.getVehicle().getVehicleId()));

        List<String> vehicleIdsNotInDeliveryTrips = vehicleDeliveryPlans
            .stream()
            .filter(vehicleDeliveryPlan -> !vehicleIdToDeliveryTrips.containsKey(vehicleDeliveryPlan.getVehicleId()))
            .map(VehicleDeliveryPlan::getVehicleId)
            .distinct()
            .collect(Collectors.toList());
        return vehicleRepo
            .findAllByVehicleIdIn(vehicleIdsNotInDeliveryTrips)
            .stream()
            .map(Vehicle::toVehicleModel)
            .collect(Collectors.toList());
    }

    @Override
    public String saveVehicleDeliveryPlan(VehicleModel.CreateDeliveryPlan createDeliveryPlan) {
        List<VehicleDeliveryPlan> vehicleDeliveryPlans = new ArrayList<>();
        Set<String> vehicleIdSet = vehicleMaintenanceHistoryRepo
            .findAllByThruDateIsNullAndVehicleIn(
                vehicleRepo.findAllByVehicleIdIn(createDeliveryPlan
                                                     .getVehicleIds()
                                                     .stream()
                                                     .distinct()
                                                     .collect(Collectors.toList())))
            .stream()
            .map(vehicleMaintenanceHistory -> vehicleMaintenanceHistory.getVehicle().getVehicleId())
            .collect(Collectors.toSet());

        for (String vehicleId : createDeliveryPlan.getVehicleIds()) {
            if (!vehicleIdSet.contains(vehicleId)) {
                continue;
            }
            vehicleDeliveryPlans.add(new VehicleDeliveryPlan(
                vehicleId,
                UUID.fromString(createDeliveryPlan.getDeliveryPlanId())
            ));
        }
        vehicleDeliveryPlanRepo.saveAll(vehicleDeliveryPlans);
        return createDeliveryPlan.getDeliveryPlanId();
    }

    @Override
    public boolean deleteVehicleDeliveryPlan(VehicleModel.DeleteDeliveryPlan deleteDeliveryPlan) {
        VehicleDeliveryPlan vehicleDeliveryPlan = vehicleDeliveryPlanRepo.findByDeliveryPlanIdAndVehicleId(
            UUID.fromString(deleteDeliveryPlan.getDeliveryPlanId()),
            deleteDeliveryPlan.getVehicleId()
        );
        if (vehicleDeliveryPlan != null) {
            vehicleDeliveryPlanRepo.delete(vehicleDeliveryPlan);
            return true;
        }
        return false;
    }

    @Override
    @Transactional
    public List<Vehicle> save(
        List<VehicleModel.Create> vehicleModels,
        List<VehicleModel.CreateLocationPriority> vehicleLocationPriorities,
        List<LocationModel.Create> shipToModels
    ) {
        List<Vehicle> listVehicles = new ArrayList<>();
        for (int i = 0; i < vehicleModels.size(); i++) {
            VehicleModel.Create vm = vehicleModels.get(i);
            Vehicle vehicle = vehicleRepo.findByVehicleId(vm.getVehicleId());
            if (vehicle == null) {
                vehicle = new Vehicle();
                vehicle.setVehicleId(vm.getVehicleId());
                vehicle.setCapacity(vm.getCapacity()); // kg
                vehicle.setDescription(vm.getDescription());
                vehicle.setHeight(vm.getHeight());
                vehicle.setLength(vm.getLength());
                vehicle.setWidth(vm.getWidth());
                vehicle.setPallet(vm.getPallet());
                vehicle.setProductTransportCategoryId(vm.getProductTransportCategoryId());
                vehicle.setPriority(vm.getPriority().equals("Y") ? 1 : 2);
                vehicle = vehicleRepo.save(vehicle);

                VehicleMaintenanceHistory vmh = vehicle.createVehicleMaintenanceHistory();
                vmh = vehicleMaintenanceHistoryRepo.save(vmh);

                listVehicles.add(vehicle);
            }
            log.info("save vehicles, finished " + i + "/" + vehicleModels.size());

        }

        Map<String, Vehicle> vehicleMap = new HashMap<>();
        Map<String, PostalAddress> postalAddressMap = new HashMap<>();

        vehicleRepo
            .findAllByVehicleIdIn(vehicleLocationPriorities
                                      .stream()
                                      .map(VehicleModel.CreateLocationPriority::getVehicleId)
                                      .collect(Collectors.toList()))
            .forEach(vehicle -> vehicleMap.put(vehicle.getVehicleId(), vehicle));
        postalAddressRepo
            .findAllByLocationCodeIn(vehicleLocationPriorities
                                         .stream()
                                         .map(VehicleModel.CreateLocationPriority::getLocationCode)
                                         .collect(Collectors.toList()))
            .forEach(postalAddress -> postalAddressMap.put(postalAddress.getLocationCode(), postalAddress));

        vehicleLocationPriorityRepo.saveAll(
            vehicleLocationPriorities.stream().map(vlp -> vlp.toVehicleLocationPriority(
                vehicleMap.get(vlp.getVehicleId()), postalAddressMap.get(vlp.getLocationCode())
            )).filter(Objects::nonNull).collect(Collectors.toList()));

        Map<String, LocationModel.Create> shipToModelMap = new HashMap<>();
        shipToModels.forEach(create -> shipToModelMap.put(
            create.getLocationCode(),
            create));

        List<PostalAddress> postalAddresses = postalAddressRepo.findAllByLocationCodeIn(new ArrayList<>(shipToModelMap.keySet()));
        postalAddresses.forEach(postalAddress -> postalAddress.setMaxLoadWeight(shipToModelMap
                                                                                    .get(postalAddress.getLocationCode())
                                                                                    .getMaxLoadWeight()));
        postalAddressRepo.saveAll(postalAddresses);

        return listVehicles;
    }
}

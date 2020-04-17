package com.hust.baseweb.applications.tms.controller;

import com.hust.baseweb.applications.tms.entity.Vehicle;
import com.hust.baseweb.applications.tms.model.LocationModel;
import com.hust.baseweb.applications.tms.model.VehicleModel;
import com.hust.baseweb.applications.tms.service.VehicleService;
import com.poiji.bind.Poiji;
import com.poiji.exception.PoijiExcelType;
import com.poiji.option.PoijiOptions;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin
@AllArgsConstructor(onConstructor = @__(@Autowired))
@Log4j2
public class VehicleAPIController {

    private VehicleService vehicleService;

    @GetMapping("/vehicle/page")
    public ResponseEntity<?> getVehicles(Principal principal, Pageable pageable) {
        log.info("::getVehicles, ");
        return ResponseEntity.ok().body(vehicleService.findAll(pageable).map(Vehicle::toVehicleModel));
    }

    @GetMapping("/vehicle/all")
    public ResponseEntity<?> getAllVehicles(Principal principal) {
        log.info("::getAllVehicles, ");
        return ResponseEntity.ok().body(vehicleService.findAll().stream()
                .map(Vehicle::toVehicleModel).collect(Collectors.toList()));
    }

    @PostMapping("/upload-vehicle")
    public ResponseEntity<?> uploadVehicles(Principal principal, @RequestParam("file") MultipartFile multipartFile) throws IOException {
        log.info("::uploadVehicle");
        List<VehicleModel.Create> vehicleModels
                = Poiji.fromExcel(multipartFile.getInputStream(), PoijiExcelType.XLSX, VehicleModel.Create.class,
                PoijiOptions.PoijiOptionsBuilder.settings().sheetName("Xe tải").build());

        List<VehicleModel.CreateLocationPriority> vehicleLocationPriorities
                = Poiji.fromExcel(multipartFile.getInputStream(),
                PoijiExcelType.XLSX,
                VehicleModel.CreateLocationPriority.class,
                PoijiOptions.PoijiOptionsBuilder.settings().sheetName("Xe tải - Shipto").build());

        List<LocationModel.Create> shipToModels
                = Poiji.fromExcel(multipartFile.getInputStream(), PoijiExcelType.XLSX, LocationModel.Create.class,
                PoijiOptions.PoijiOptionsBuilder.settings().sheetName("Shipto").build());

        /*
        List<Vehicle> vehicles = vehicleModels.stream().map(CreateVehicleModel::toVehicle).collect(Collectors.toList());
        List<VehicleMaintenanceHistory> vehicleMaintenanceHistories = vehicles.stream().map(Vehicle::createVehicleMaintenanceHistory).collect(Collectors.toList());
        vehicleService.saveAll(vehicles);
        vehicleService.saveAllMaintenanceHistory(vehicleMaintenanceHistories);
        */
        List<Vehicle> vehicles = vehicleService.save(vehicleModels, vehicleLocationPriorities, shipToModels);

        return ResponseEntity.ok().body(vehicles.size());
    }

    // list view
    @GetMapping("/vehicle/{deliveryPlanId}/page")
    public ResponseEntity<?> getVehicle(Principal principal, @PathVariable String deliveryPlanId, Pageable pageable) {
        log.info("::getVehicle deliveryPlanId=" + deliveryPlanId);
        return ResponseEntity.ok().body(vehicleService.findAllInDeliveryPlan(deliveryPlanId, pageable));
    }

    // list view
    @GetMapping("/vehicle/{deliveryPlanId}/all")
    public ResponseEntity<?> getAllVehicle(Principal principal, @PathVariable String deliveryPlanId) {
        log.info("::getVehicle deliveryPlanId=" + deliveryPlanId);
        return ResponseEntity.ok().body(vehicleService.findAllInDeliveryPlan(deliveryPlanId));
    }

    // submit button
    @PostMapping("/create-vehicle-delivery-plan")
    public ResponseEntity<?> createVehicleDeliveryPlan(Principal principal,
                                                       @RequestBody VehicleModel.CreateDeliveryPlan createDeliveryPlan) {
        log.info("::createVehicleDeliveryPlan: " + createDeliveryPlan.getDeliveryPlanId());
        return ResponseEntity.ok().body(vehicleService.saveVehicleDeliveryPlan(createDeliveryPlan));
    }

    // delete button
    @PostMapping("/delete-vehicle-delivery-plan")
    public ResponseEntity<?> deleteVehicleDeliveryPlan(Principal principal,
                                                       @RequestBody VehicleModel.DeleteDeliveryPlan deleteDeliveryPlan) {
        log.info("::deleteVehicleDeliveryPlan: " + deleteDeliveryPlan.getDeliveryPlanId());
        return ResponseEntity.ok().body(vehicleService.deleteVehicleDeliveryPlan(deleteDeliveryPlan));
    }

    // add view
    @GetMapping("/vehicle-not-in/{deliveryPlanId}/page")
    public ResponseEntity<?> getVehiclePageNotIn(Principal principal,
                                                 @PathVariable String deliveryPlanId,
                                                 Pageable pageable) {
        log.info("::getVehicleNotIn deliveryPlanId=" + deliveryPlanId);
        return ResponseEntity.ok().body(vehicleService.findAllNotInDeliveryPlan(deliveryPlanId, pageable));
    }

    // add view
    @GetMapping("/vehicle-not-in/{deliveryPlanId}/all")
    public ResponseEntity<?> getAllVehicleNotIn(Principal principal, @PathVariable String deliveryPlanId) {
        log.info("::getVehicleNotIn deliveryPlanId=" + deliveryPlanId);
        return ResponseEntity.ok().body(vehicleService.findAllNotInDeliveryPlan(deliveryPlanId));
    }
}

package com.hust.baseweb.applications.tms.controller;

import com.hust.baseweb.applications.tms.entity.Vehicle;
import com.hust.baseweb.applications.tms.model.createvehicle.CreateVehicleModel;
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
import java.util.stream.StreamSupport;

@RestController
@CrossOrigin
@AllArgsConstructor(onConstructor = @__(@Autowired))
@Log4j2
@RequestMapping("/vehicle")
public class VehicleAPIController {

    private VehicleService vehicleService;

    @GetMapping("")
    public ResponseEntity<?> getVehicles(Principal principal, Pageable pageable) {
        log.info("::getVehicles, ");
        return ResponseEntity.ok().body(vehicleService.findAll(pageable).map(Vehicle::toVehicleModel));
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllVehicles(Principal principal) {
        log.info("::getAllVehicles, ");
        return ResponseEntity.ok().body(StreamSupport.stream(vehicleService.findAll().spliterator(), false)
                .map(Vehicle::toVehicleModel).collect(Collectors.toList()));
    }

    @PostMapping("/upload")
    public ResponseEntity<?> uploadVehicles(Principal principal, @RequestParam("file") MultipartFile multipartFile) throws IOException {
        log.info("::uploadVehicle");
        List<CreateVehicleModel> vehicleModels =
                Poiji.fromExcel(multipartFile.getInputStream(), PoijiExcelType.XLSX, CreateVehicleModel.class,
                        PoijiOptions.PoijiOptionsBuilder.settings().sheetIndex(0).build());

        vehicleService.saveAll(vehicleModels.stream().map(CreateVehicleModel::toVehicle).collect(Collectors.toList()));
        return ResponseEntity.ok().build();
    }
}

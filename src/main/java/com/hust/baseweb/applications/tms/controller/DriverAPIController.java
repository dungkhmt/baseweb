package com.hust.baseweb.applications.tms.controller;

import com.hust.baseweb.applications.tms.entity.DeliveryTrip;
import com.hust.baseweb.applications.tms.entity.PartyDriver;
import com.hust.baseweb.applications.tms.model.DriverModel;
import com.hust.baseweb.applications.tms.repo.DeliveryTripRepo;
import com.hust.baseweb.applications.tms.repo.PartyDriverRepo;
import com.hust.baseweb.applications.tms.service.PartyDriverService;
import com.hust.baseweb.entity.Person;
import com.hust.baseweb.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.stream.Collectors;


@RestController
@CrossOrigin
@AllArgsConstructor(onConstructor = @__(@Autowired))
@Log4j2
public class DriverAPIController {

    private PartyDriverService partyDriverService;
    private PartyDriverRepo partyDriverRepo;
    private UserService userService;
    private DeliveryTripRepo deliveryTripRepo;

    @PostMapping("/create-driver")
    public ResponseEntity<?> createDriver(Principal principal, @RequestBody DriverModel.InputCreate input) {
        PartyDriver driver = partyDriverService.save(input);
        return ResponseEntity.ok().body(driver);
    }

    @GetMapping("/get-all-drivers")
    public ResponseEntity<?> findAllDrivers() {
        log.info("::findAllDrivers()");
        List<PartyDriver> drivers = partyDriverService.findAll();
        return ResponseEntity.ok().body(drivers.stream()
                .map(partyDriver -> partyDriver.getPerson().getBasicInfoModel()).collect(Collectors.toList()));
    }

    @GetMapping("/get-driver-in-delivery-trip/{deliveryTripId}")
    public ResponseEntity<?> findDriver(@PathVariable String deliveryTripId) {
        log.info("::findDriver(), deliveryTripId=" + deliveryTripId);
        DeliveryTrip deliveryTrip = deliveryTripRepo.findById(UUID.fromString(deliveryTripId))
                .orElseThrow(NoSuchElementException::new);
        PartyDriver partyDriver = deliveryTrip.getPartyDriver();
        if (partyDriver == null || partyDriver.getPerson() == null) {
            return ResponseEntity.ok().body(new Person.BasicInfoModel());
        }
        return ResponseEntity.ok().body(partyDriver.getPerson().getBasicInfoModel());
    }

    @GetMapping("/set-driver-to-delivery-trip/{deliveryTripId}/{driverPartyId}")
    public ResponseEntity<?> setDriverToDeliveryTrip(@PathVariable String deliveryTripId,
                                                     @PathVariable String driverPartyId) {
        log.info("::setDriverToDeliveryTrip(), deliveryTripId=" + deliveryTripId + ", driverPartyId=" + driverPartyId);
        DeliveryTrip deliveryTrip = deliveryTripRepo.findById(UUID.fromString(deliveryTripId))
                .orElseThrow(NoSuchElementException::new);
        if (driverPartyId.equals("unSelected")) {
            deliveryTrip.setPartyDriver(null);
        } else {
            PartyDriver partyDriver = partyDriverRepo.findById(UUID.fromString(driverPartyId))
                    .orElseThrow(NoSuchElementException::new);
            deliveryTrip.setPartyDriver(partyDriver);
        }
        deliveryTrip = deliveryTripRepo.save(deliveryTrip);
        return ResponseEntity.ok().body(deliveryTrip.toDeliveryTripModel());
    }
}

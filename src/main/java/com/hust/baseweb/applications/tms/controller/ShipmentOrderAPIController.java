package com.hust.baseweb.applications.tms.controller;

import com.hust.baseweb.applications.tms.entity.DeliveryPlan;
import com.hust.baseweb.applications.tms.entity.DeliveryTrip;
import com.hust.baseweb.applications.tms.entity.DeliveryTripDetail;
import com.hust.baseweb.applications.tms.entity.Shipment;
import com.hust.baseweb.applications.tms.model.createdeliveryplan.CreateDeliveryPlanInputModel;
import com.hust.baseweb.applications.tms.model.createdeliverytrip.CreateDeliveryTripDetailInputModel;
import com.hust.baseweb.applications.tms.model.createdeliverytrip.CreateDeliveryTripInputModel;
import com.hust.baseweb.applications.tms.model.shipmentorder.CreateShipmentInputModel;
import com.hust.baseweb.applications.tms.service.DeliveryPlanService;
import com.hust.baseweb.applications.tms.service.DeliveryTripDetailService;
import com.hust.baseweb.applications.tms.service.DeliveryTripService;
import com.hust.baseweb.applications.tms.service.ShipmentService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@CrossOrigin
@AllArgsConstructor(onConstructor = @__(@Autowired))
@Log4j2
public class ShipmentOrderAPIController {

    private ShipmentService shipmentService;
    private DeliveryPlanService deliveryPlanService;
    private DeliveryTripService deliveryTripService;
    private DeliveryTripDetailService deliveryTripDetailService;

    @PostMapping("/create-shipment")
    public ResponseEntity<?> createOrderShipment(Principal principal, @RequestBody CreateShipmentInputModel input) {
        log.info("::createOrderShipment, shipment-items = " + input.getShipmentItems().length);

        Shipment shipment = shipmentService.save(input);
        return ResponseEntity.ok().body(shipment);
    }

    @PostMapping("/create-delivery-plan")
    public ResponseEntity<?> createDeliveryPlan(Principal principal, @RequestBody CreateDeliveryPlanInputModel input) {
        log.info("createDeliveryPlan....");

        DeliveryPlan deliveryPlan = deliveryPlanService.save(input);

        return ResponseEntity.ok().body(deliveryPlan);
    }

    @GetMapping("/delivery-plan")
    public ResponseEntity<?> getDeliveryPlanList(Pageable pageable) {
        log.info("getDeliveryPlan....");

        return ResponseEntity.ok().body(deliveryPlanService.findAll(pageable));
    }

    @PostMapping("/create-delivery-trip")
    public ResponseEntity<?> createDeliveryTrip(Principal principal, @RequestBody CreateDeliveryTripInputModel input) {
        DeliveryTrip deliveryTrip = deliveryTripService.save(input);
        return ResponseEntity.ok().body(deliveryTrip);
    }

    @PostMapping("/create-delivery-trip-detail")
    public ResponseEntity<?> createDeliveryTripDetail(Principal principal, @RequestBody CreateDeliveryTripDetailInputModel input) {
        DeliveryTripDetail deliveryTripDetail = null;
        deliveryTripDetail = deliveryTripDetailService.save(input);
        return ResponseEntity.ok().body(deliveryTripDetail);
    }
}

package com.hust.baseweb.applications.tms.controller;

import com.hust.baseweb.applications.tms.entity.*;
import com.hust.baseweb.applications.tms.model.createdeliveryplan.CreateDeliveryPlanInputModel;
import com.hust.baseweb.applications.tms.model.createdeliverytrip.CreateDeliveryTripDetailInputModel;
import com.hust.baseweb.applications.tms.model.createdeliverytrip.CreateDeliveryTripInputModel;
import com.hust.baseweb.applications.tms.model.shipmentitem.CreateShipmentItemDeliveryPlan;
import com.hust.baseweb.applications.tms.model.shipmentorder.CreateShipmentInputModel;
import com.hust.baseweb.applications.tms.model.shipmentorder.CreateShipmentItemInputModel;
import com.hust.baseweb.applications.tms.service.*;
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
import java.util.UUID;

@RestController
@CrossOrigin
@AllArgsConstructor(onConstructor = @__(@Autowired))
@Log4j2
public class ShipmentOrderAPIController {

    private ShipmentService shipmentService;
    private ShipmentItemService shipmentItemService;
    private DeliveryPlanService deliveryPlanService;
    private DeliveryTripService deliveryTripService;
    private DeliveryTripDetailService deliveryTripDetailService;

    @PostMapping("/create-shipment")
    public ResponseEntity<?> createOrderShipment(Principal principal, @RequestBody CreateShipmentInputModel input) {
        log.info("::createOrderShipment, shipment-items = " + input.getShipmentItems().length);

        Shipment shipment = shipmentService.save(input);
        return ResponseEntity.ok().body(shipment);
    }

    @PostMapping("/shipment/upload")
    public ResponseEntity<?> uploadOrderShipment(Principal principal, @RequestParam("file") MultipartFile multipartFile) throws IOException {
        log.info("::uploadOrderShipment");
        List<CreateShipmentItemInputModel> shipmentItemInputModels =
                Poiji.fromExcel(multipartFile.getInputStream(), PoijiExcelType.XLSX, CreateShipmentItemInputModel.class,
                        PoijiOptions.PoijiOptionsBuilder.settings().sheetIndex(0).build());

        Shipment shipment = shipmentService.save(new CreateShipmentInputModel(shipmentItemInputModels.toArray(new CreateShipmentItemInputModel[0])));
        return ResponseEntity.ok().body(shipment);
    }


    @GetMapping("/shipment")
    public ResponseEntity<?> getOrderShipment(Principal principal, Pageable pageable) {
        log.info("::getOrderShipment, ");
        return ResponseEntity.ok().body(shipmentService.findAll(pageable));
    }

    @GetMapping("/shipment-item")
    public ResponseEntity<?> getOrderShipmentItem(Principal principal, Pageable pageable) {
        log.info("::getOrderShipmentItem, ");
        return ResponseEntity.ok().body(shipmentItemService.findAll(pageable).map(ShipmentItem::toShipmentItemModel));
    }

    @GetMapping("/shipment-item/{deliveryPlanId}")
    public ResponseEntity<?> getOrderShipmentItem(Principal principal, @PathVariable String deliveryPlanId) {
        log.info("::getOrderShipmentItem deliveryPlanId=" + deliveryPlanId);
        return ResponseEntity.ok().body(shipmentItemService.findAllByDeliveryPlanId(deliveryPlanId));
    }

    @PostMapping("/create-delivery-plan")
    public ResponseEntity<?> createDeliveryPlan(Principal principal, @RequestBody CreateDeliveryPlanInputModel input) {
        log.info("createDeliveryPlan....");
        DeliveryPlan deliveryPlan = deliveryPlanService.save(input);
        return ResponseEntity.ok().body(deliveryPlan);
    }

    @GetMapping("/delivery-plan")
    public ResponseEntity<?> getDeliveryPlanList(Pageable pageable) {
        log.info("getDeliveryPlanList....");
        return ResponseEntity.ok().body(deliveryPlanService.findAll(pageable));
    }

    @GetMapping("/delivery-plan/{deliveryPlanId}")
    public ResponseEntity<?> getDeliveryPlan(@PathVariable String deliveryPlanId) {
        log.info("getDeliveryPlan: " + deliveryPlanId);
        return ResponseEntity.ok().body(deliveryPlanService.findById(UUID.fromString(deliveryPlanId)));
    }

    @GetMapping("/delivery-trip")
    public ResponseEntity<?> getDeliveryTripList(Pageable pageable) {
        log.info("getDeliveryTripList....");
        return ResponseEntity.ok().body(deliveryTripService.findAll(pageable).map(DeliveryTrip::toDeliveryTripModel));
    }

    @GetMapping("/delivery-trip/{delivery-trip-id}")
    public ResponseEntity<?> getDeliveryTrip(@PathVariable("delivery-trip-id") String deliveryTripId) {
        log.info("getDeliveryTrip: " + deliveryTripId);
        return ResponseEntity.ok().body(deliveryTripService.findById(UUID.fromString(deliveryTripId)));
    }

    @GetMapping("/delivery-trip-detail")
    public ResponseEntity<?> getDeliveryTripDetailList(Pageable pageable) {
        log.info("getDeliveryTripDetailList....");
        return ResponseEntity.ok().body(deliveryTripDetailService.findAll(pageable));
    }

    @PostMapping("/create-delivery-trip")
    public ResponseEntity<?> createDeliveryTrip(Principal principal, @RequestBody CreateDeliveryTripInputModel input) {
        log.info("::createDeliveryTrip: " + input);
        DeliveryTrip deliveryTrip = deliveryTripService.save(input);
        return ResponseEntity.ok().body(deliveryTrip);
    }

    @PostMapping("/create-delivery-trip-detail")
    public ResponseEntity<?> createDeliveryTripDetail(Principal principal, @RequestBody CreateDeliveryTripDetailInputModel input) {
        log.info("::createDeliveryTripDetail: " + input);
        DeliveryTripDetail deliveryTripDetail;
        deliveryTripDetail = deliveryTripDetailService.save(input);
        return ResponseEntity.ok().body(deliveryTripDetail);
    }

    @PostMapping("/create-shipment-item-delivery-plan")
    public ResponseEntity<?> createShipmentItemDeliveryPlan(Principal principal, @RequestBody CreateShipmentItemDeliveryPlan createShipmentItemDeliveryPlan) {
        log.info("::createShipmentItemDeliveryPlan: " + createShipmentItemDeliveryPlan.getDeliveryPlanId());
        return ResponseEntity.ok().body(shipmentItemService.saveShipmentItemDeliveryPlan(createShipmentItemDeliveryPlan));
    }
}

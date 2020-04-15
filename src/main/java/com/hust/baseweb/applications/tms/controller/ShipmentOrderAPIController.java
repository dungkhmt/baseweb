package com.hust.baseweb.applications.tms.controller;

import com.hust.baseweb.applications.tms.entity.DeliveryPlan;
import com.hust.baseweb.applications.tms.entity.DeliveryTrip;
import com.hust.baseweb.applications.tms.entity.Shipment;
import com.hust.baseweb.applications.tms.entity.ShipmentItem;
import com.hust.baseweb.applications.tms.model.*;
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
import java.text.ParseException;
import java.util.ArrayList;
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
    public ResponseEntity<?> createOrderShipment(Principal principal,
                                                 @RequestBody ShipmentModel.CreateShipmentInputModel input) throws ParseException {
        log.info("::createOrderShipment, shipment-items = " + input.getShipmentItems().length);

        Shipment shipment = shipmentService.save(input);
        return ResponseEntity.ok().body(shipment);
    }

    @PostMapping("/create-shipment-item")
    public ResponseEntity<?> createOrderShipment(Principal principal, @RequestBody ShipmentItemModel.Create input) {
        log.info("::createOrderShipment");

        Shipment shipment = shipmentService.save(input);
        return ResponseEntity.ok().body(shipment);
    }

    @PostMapping("/shipment/upload")
    public ResponseEntity<?> uploadOrderShipment(Principal principal,
                                                 @RequestParam("file") MultipartFile multipartFile) throws IOException, ParseException {
        log.info("::uploadOrderShipment");
        List<ShipmentItemModel.Create> shipmentItemInputModels =
                Poiji.fromExcel(multipartFile.getInputStream(), PoijiExcelType.XLSX, ShipmentItemModel.Create.class,
                        PoijiOptions.PoijiOptionsBuilder.settings().sheetIndex(0).build());

        Shipment shipment = shipmentService.save(new ShipmentModel.CreateShipmentInputModel(shipmentItemInputModels.toArray(
                new ShipmentItemModel.Create[0])));
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

    @GetMapping("/shipment-item-delivery-plan/{deliveryPlanId}/page")
    public ResponseEntity<?> getPageOrderShipmentItem(Principal principal,
                                                      @PathVariable String deliveryPlanId,
                                                      Pageable pageable) {
        log.info("::getOrderShipmentItem deliveryPlanId=" + deliveryPlanId);
        return ResponseEntity.ok().body(shipmentItemService.findAllInDeliveryPlanId(deliveryPlanId, pageable));
    }

    @GetMapping("/shipment-item-delivery-plan/{deliveryPlanId}/all")
    public ResponseEntity<?> getAllOrderShipmentItemDeliveryPlan(Principal principal,
                                                                 @PathVariable String deliveryPlanId) {
        log.info("::getOrderShipmentItem deliveryPlanId=" + deliveryPlanId);
        return ResponseEntity.ok().body(shipmentItemService.findAllInDeliveryPlanId(deliveryPlanId));
    }

    @GetMapping("/shipment-item-delivery-trip/{deliveryTripId}/all")
    public ResponseEntity<?> getAllOrderShipmentItemDeliveryTrip(Principal principal,
                                                                 @PathVariable String deliveryTripId) {
        log.info("::getOrderShipmentItem deliveryTripId=" + deliveryTripId);
        return ResponseEntity.ok().body(shipmentItemService.findAllInDeliveryPlanIdNearestDeliveryTrip(deliveryTripId));
    }

    @GetMapping("/shipment-item-not-in-delivery-plan/{deliveryPlanId}/page")
    public ResponseEntity<?> getAllOrderShipmentItemNotIn(Principal principal,
                                                          @PathVariable String deliveryPlanId,
                                                          Pageable pageable) {
        log.info("::getAllOrderShipmentItemNotIn deliveryPlanId=" + deliveryPlanId);
        return ResponseEntity.ok().body(shipmentItemService.findAllNotInDeliveryPlanId(deliveryPlanId, pageable));
    }

    @GetMapping("/shipment-item-not-in-delivery-plan/{deliveryPlanId}/all")
    public ResponseEntity<?> getOrderShipmentItemPageNotIn(Principal principal, @PathVariable String deliveryPlanId) {
        log.info("::getOrderShipmentItemPageNotIn deliveryPlanId=" + deliveryPlanId);
        return ResponseEntity.ok().body(shipmentItemService.findAllNotInDeliveryPlanId(deliveryPlanId));
    }

    @PostMapping("/delete-shipment-item-delivery-plan")
    public ResponseEntity<?> deleteShipmentItemDeliveryPlan(Principal principal,
                                                            @RequestBody ShipmentItemModel.DeleteDeliveryPlan deleteVehicleDeliveryPlanModel) {
        log.info("::deleteShipmentItemDeliveryPlan: " + deleteVehicleDeliveryPlanModel.getDeliveryPlanId());
        return ResponseEntity.ok()
                .body(shipmentItemService.deleteShipmentItemDeliveryPlan(deleteVehicleDeliveryPlanModel));
    }

    @PostMapping("/create-delivery-plan")
    public ResponseEntity<?> createDeliveryPlan(Principal principal, @RequestBody DeliveryPlanModel.Create input) {
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

    @GetMapping("/delivery-trip/{deliveryPlanId}/page")
    public ResponseEntity<?> getPageDeliveryTripList(Pageable pageable, @PathVariable String deliveryPlanId) {
        log.info("getPageDeliveryTripList, deliveryPlanId=" + deliveryPlanId);
        return ResponseEntity.ok().body(deliveryTripService.findAllByDeliveryPlanId(deliveryPlanId, pageable));
    }

    @GetMapping("/delivery-trip/{deliveryPlanId}/all")
    public ResponseEntity<?> getAllDeliveryTripList(@PathVariable String deliveryPlanId) {
        log.info("getAllDeliveryTripList, deliveryPlanId=" + deliveryPlanId);
        return ResponseEntity.ok().body(deliveryTripService.findAllByDeliveryPlanId(deliveryPlanId));
    }

    @GetMapping("/delivery-trip/{deliveryTripId}/basic-info")
    public ResponseEntity<?> getDeliveryTrip(@PathVariable String deliveryTripId) {
        log.info("getDeliveryTrip: " + deliveryTripId);
        return ResponseEntity.ok().body(deliveryTripService.findById(UUID.fromString(deliveryTripId)));
    }

    @GetMapping("/delivery-trip-detail/{deliveryTripId}")
    public ResponseEntity<?> getDeliveryTripDetailList(@PathVariable String deliveryTripId) {
        log.info("getDeliveryTripDetailList, deliveryTripId=" + deliveryTripId);
        return ResponseEntity.ok().body(deliveryTripDetailService.findAll(deliveryTripId));
    }

    @PostMapping("/create-delivery-trip")
    public ResponseEntity<?> createDeliveryTrip(Principal principal,
                                                @RequestBody com.hust.baseweb.applications.tms.model.DeliveryTripModel.Create input) {
        log.info("::createDeliveryTrip: " + input);
        DeliveryTrip deliveryTrip = deliveryTripService.save(input, 0, 0, 0, 0, 0, 0, 0);
        return ResponseEntity.ok().body(deliveryTrip);
    }

    @PostMapping("/create-delivery-trip-detail/{deliveryTripId}")
    public ResponseEntity<?> createDeliveryTripDetail(Principal principal,
                                                      @RequestBody List<DeliveryTripDetailModel.Create> inputs,
                                                      @PathVariable String deliveryTripId) {
        log.info("::createDeliveryTripDetail: " + deliveryTripId);
        return ResponseEntity.ok().body(deliveryTripDetailService.save(deliveryTripId, inputs));
    }

    @GetMapping("/delete-delivery-trip-detail/{deliveryTripDetailId}")
    public ResponseEntity<?> deleteDeliveryTripDetail(Principal principal,
                                                      @PathVariable String deliveryTripDetailId) {
        log.info("::deleteDeliveryTripDetail: " + deliveryTripDetailId);
        return ResponseEntity.ok().body(deliveryTripDetailService.delete(deliveryTripDetailId));
    }

    @PostMapping("/create-shipment-item-delivery-plan")
    public ResponseEntity<?> createShipmentItemDeliveryPlan(Principal principal,
                                                            @RequestBody ShipmentItemModel.CreateDeliveryPlan createDeliveryPlan) {
        log.info("::createShipmentItemDeliveryPlan: " + createDeliveryPlan.getDeliveryPlanId());
        return ResponseEntity.ok()
                .body(shipmentItemService.saveShipmentItemDeliveryPlan(createDeliveryPlan));
    }

    @PostMapping("/delivery-trip/{deliveryTripId}/capacity-info")
    public ResponseEntity<?> getDeliveryTripCapacityInfo(@PathVariable String deliveryTripId,
                                                         @RequestBody List<DeliveryTripDetailModel.Create> shipmentItemModels) {
        log.info("::getDeliveryTripCapacityInfo(): deliveryTripId=" + deliveryTripId);
        return ResponseEntity.ok().body(deliveryTripService.getDeliveryTripInfo(deliveryTripId, shipmentItemModels));
    }

    @PostMapping("/delivery-trips/chart-info")
    public ResponseEntity<?> getDeliveryTripsChartInfo(@RequestBody List<String> deliveryTripIds) {
        log.info("::getDeliveryTripsChartInfo(), deliveryTripIdsSize=" + deliveryTripIds.size());

        List<DeliveryTripModel> deliveryTripModels = new ArrayList<>();

        for (String deliveryTripId : deliveryTripIds) {
            DeliveryTripModel deliveryTripModel = deliveryTripService.findById(UUID.fromString(deliveryTripId));
            DeliveryTripModel.Tour tour = deliveryTripService.getDeliveryTripInfo(deliveryTripId,
                    new ArrayList<>());
            deliveryTripModel.setTotalDistance(tour.getTotalDistance());
            deliveryTripModel.setTotalWeight(tour.getTotalWeight());
            deliveryTripModels.add(deliveryTripModel);
        }

        return ResponseEntity.ok(deliveryTripModels);
    }


    @GetMapping("/approve-delivery-trip/{deliveryTripId}")
    public ResponseEntity<?> approveDeliveryTrip(@PathVariable String deliveryTripId) {
        return ResponseEntity.ok().body(deliveryTripService.approveDeliveryTrip(UUID.fromString(deliveryTripId)));
    }

    @GetMapping("/start-execute-delivery-trip/{deliveryTripId}")
    public ResponseEntity<?> startExecuteDeliveryTrip(@PathVariable String deliveryTripId) {
        return ResponseEntity.ok().body(deliveryTripService.startExecuteDeliveryTrip(UUID.fromString(deliveryTripId)));
    }

    @GetMapping("/complete-delivery-trip-detail/{deliveryTripDetailId}")
    public ResponseEntity<?> completeDeliveryTripDetail(@PathVariable String deliveryTripDetailId) {
        return ResponseEntity.ok()
                .body(deliveryTripDetailService.completeDeliveryTripDetail(UUID.fromString(deliveryTripDetailId)));
    }
}

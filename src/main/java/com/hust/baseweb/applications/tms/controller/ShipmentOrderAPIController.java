package com.hust.baseweb.applications.tms.controller;

import com.hust.baseweb.applications.tms.entity.DeliveryPlan;
import com.hust.baseweb.applications.tms.entity.DeliveryTrip;
import com.hust.baseweb.applications.tms.entity.Shipment;
import com.hust.baseweb.applications.tms.entity.ShipmentItem;
import com.hust.baseweb.applications.tms.model.createdeliveryplan.CreateDeliveryPlanInputModel;
import com.hust.baseweb.applications.tms.model.createdeliverytrip.CreateDeliveryTripDetailInputModel;
import com.hust.baseweb.applications.tms.model.createdeliverytrip.CreateDeliveryTripInputModel;
import com.hust.baseweb.applications.tms.model.deliverytrip.DeliveryTripInfoModel;
import com.hust.baseweb.applications.tms.model.deliverytrip.DeliveryTripModel;
import com.hust.baseweb.applications.tms.model.shipmentitem.CreateShipmentItemDeliveryPlanModel;
import com.hust.baseweb.applications.tms.model.shipmentitem.DeleteShipmentItemDeliveryPlanModel;
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
    public ResponseEntity<?> createOrderShipment(Principal principal, @RequestBody CreateShipmentInputModel input) {
        log.info("::createOrderShipment, shipment-items = " + input.getShipmentItems().length);

        Shipment shipment = shipmentService.save(input);
        return ResponseEntity.ok().body(shipment);
    }

    @PostMapping("/create-shipment-item")
    public ResponseEntity<?> createOrderShipment(Principal principal, @RequestBody CreateShipmentItemInputModel input) {
        log.info("::createOrderShipment");

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

    @GetMapping("/shipment-item/{deliveryPlanId}/page")
    public ResponseEntity<?> getPageOrderShipmentItem(Principal principal, @PathVariable String deliveryPlanId, Pageable pageable) {
        log.info("::getOrderShipmentItem deliveryPlanId=" + deliveryPlanId);
        return ResponseEntity.ok().body(shipmentItemService.findAllInDeliveryPlanId(deliveryPlanId, pageable));
    }

    @GetMapping("/shipment-item/{deliveryTripId}/all")
    public ResponseEntity<?> getAllOrderShipmentItem(Principal principal, @PathVariable String deliveryTripId) {
        log.info("::getOrderShipmentItem deliveryTripId=" + deliveryTripId);
        return ResponseEntity.ok().body(shipmentItemService.findAllInDeliveryPlanIdNearestDeliveryTrip(deliveryTripId));
    }

    @GetMapping("/shipment-item-not-in/{deliveryPlanId}")
    public ResponseEntity<?> getOrderShipmentItemNotIn(Principal principal, @PathVariable String deliveryPlanId, Pageable pageable) {
        log.info("::getOrderShipmentItemNotIn deliveryPlanId=" + deliveryPlanId);
        return ResponseEntity.ok().body(shipmentItemService.findAllNotInDeliveryPlanId(deliveryPlanId, pageable));
    }

    @PostMapping("/delete-shipment-item-delivery-plan")
    public ResponseEntity<?> deleteShipmentItemDeliveryPlan(Principal principal, @RequestBody DeleteShipmentItemDeliveryPlanModel deleteVehicleDeliveryPlanModel) {
        log.info("::deleteShipmentItemDeliveryPlan: " + deleteVehicleDeliveryPlanModel.getDeliveryPlanId());
        return ResponseEntity.ok().body(shipmentItemService.deleteShipmentItemDeliveryPlan(deleteVehicleDeliveryPlanModel));
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
    public ResponseEntity<?> createDeliveryTrip(Principal principal, @RequestBody CreateDeliveryTripInputModel input) {
        log.info("::createDeliveryTrip: " + input);
        DeliveryTrip deliveryTrip = deliveryTripService.save(input);
        return ResponseEntity.ok().body(deliveryTrip);
    }

    @PostMapping("/create-delivery-trip-detail/{deliveryTripId}")
    public ResponseEntity<?> createDeliveryTripDetail(Principal principal,
                                                      @RequestBody List<CreateDeliveryTripDetailInputModel> inputs,
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
                                                            @RequestBody CreateShipmentItemDeliveryPlanModel createShipmentItemDeliveryPlanModel) {
        log.info("::createShipmentItemDeliveryPlan: " + createShipmentItemDeliveryPlanModel.getDeliveryPlanId());
        return ResponseEntity.ok().body(shipmentItemService.saveShipmentItemDeliveryPlan(createShipmentItemDeliveryPlanModel));
    }

    @PostMapping("/delivery-trip/{deliveryTripId}/capacity-info")
    public ResponseEntity<?> getDeliveryTripCapacityInfo(@PathVariable String deliveryTripId,
                                                         @RequestBody List<CreateDeliveryTripDetailInputModel> shipmentItemModels) {
        log.info("::getDeliveryTripCapacityInfo(): deliveryTripId=" + deliveryTripId);
        return ResponseEntity.ok().body(deliveryTripService.getDeliveryTripInfo(deliveryTripId, shipmentItemModels));
    }

    @PostMapping("/delivery-trips/chart-info")
    public ResponseEntity<?> getDeliveryTripsChartInfo(@RequestBody List<String> deliveryTripIds) {
        log.info("::getDeliveryTripsChartInfo(), deliveryTripIdsSize=" + deliveryTripIds.size());

        List<DeliveryTripModel> deliveryTripModels = new ArrayList<>();

        for (String deliveryTripId : deliveryTripIds) {
            DeliveryTrip deliveryTrip = deliveryTripService.findById(UUID.fromString(deliveryTripId));
            DeliveryTripInfoModel deliveryTripInfoModel = deliveryTripService.getDeliveryTripInfo(deliveryTripId, new ArrayList<>());
            DeliveryTripModel deliveryTripModel = new DeliveryTripModel();
            deliveryTripModel.setDeliveryTripId(deliveryTripId);
            deliveryTripModel.setMaxVehicleCapacity(deliveryTrip.getVehicle().getCapacity());
            deliveryTripModel.setTotalDistance(deliveryTripInfoModel.getTotalDistance());
            deliveryTripModel.setTotalWeight(deliveryTripInfoModel.getTotalWeight());
            deliveryTripModels.add(deliveryTripModel);
        }

        return ResponseEntity.ok(deliveryTripModels);
    }

}

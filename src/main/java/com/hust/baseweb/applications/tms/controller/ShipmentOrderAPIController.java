package com.hust.baseweb.applications.tms.controller;

import com.hust.baseweb.applications.tms.entity.DeliveryPlan;
import com.hust.baseweb.applications.tms.entity.DeliveryTrip;
import com.hust.baseweb.applications.tms.entity.Shipment;
import com.hust.baseweb.applications.tms.entity.ShipmentItem;
import com.hust.baseweb.applications.tms.model.*;
import com.hust.baseweb.applications.tms.service.*;
import com.hust.baseweb.entity.UserLogin;
import com.hust.baseweb.service.UserService;
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
    private UserService userService;

    @PostMapping("/create-shipment")
    public ResponseEntity<?> createOrderShipment(
        Principal principal,
        @RequestBody ShipmentModel.CreateShipmentInputModel input
    ) {

        UserLogin userLogin = userService.findById(principal.getName());
        log.info("::createOrderShipment, shipment-items = " + input.getShipmentItems().length);

        Shipment shipment = shipmentService.save(userLogin, input);
        return ResponseEntity.ok().body(shipment);
    }

    @PostMapping("/create-shipment-item")
    public ResponseEntity<?> createOrderShipment(Principal principal, @RequestBody ShipmentItemModel.Create input) {
        UserLogin userLogin = userService.findById(principal.getName());
        log.info("::createOrderShipment");

        Shipment shipment = shipmentService.save(
            userLogin,
            new ShipmentModel.CreateShipmentInputModel(new ShipmentItemModel.Create[]{input}));
        return ResponseEntity.ok().body(shipment);
    }

    @PostMapping("/shipment/upload")
    public ResponseEntity<?> uploadOrderShipment(
        Principal principal,
        @RequestParam("file") MultipartFile multipartFile
    ) throws IOException, ParseException {

        UserLogin userLogin = userService.findById(principal.getName());

        log.info("::uploadOrderShipment");
        List<ShipmentItemModel.Create> shipmentItemInputModels =
            Poiji.fromExcel(multipartFile.getInputStream(), PoijiExcelType.XLSX, ShipmentItemModel.Create.class,
                            PoijiOptions.PoijiOptionsBuilder.settings().sheetIndex(0).build());

        Shipment shipment = shipmentService.save(
            userLogin,
            new ShipmentModel.CreateShipmentInputModel(shipmentItemInputModels.toArray(
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
        UserLogin userLogin = userService.findById(principal.getName());
        return ResponseEntity
            .ok()
            .body(shipmentItemService.findAll(pageable, userLogin).map(ShipmentItem::toShipmentItemModel));
    }

    @GetMapping("/shipment-item-of-user-login")
    public ResponseEntity<?> getOrderShipmentItemOfUserLogin(Principal principal, Pageable pageable) {
        log.info("::getOrderShipmentItem, ");
        UserLogin userLogin = userService.findById(principal.getName());

        return ResponseEntity.ok().body(shipmentItemService.findAllByUserLogin(userLogin, pageable));
        //return ResponseEntity.ok().body(shipmentItemService.findAll(pageable).map(ShipmentItem::toShipmentItemModel));
    }


    @GetMapping("/shipment-item-delivery-plan/{deliveryPlanId}/page")
    public ResponseEntity<?> getPageOrderShipmentItem(
        Principal principal,
        @PathVariable String deliveryPlanId,
        Pageable pageable
    ) {
        log.info("::getOrderShipmentItem deliveryPlanId=" + deliveryPlanId);

        UserLogin userLogin = userService.findById(principal.getName());

        return ResponseEntity.ok().body(shipmentItemService.findAllInDeliveryPlan(deliveryPlanId, pageable, userLogin));
    }

    @GetMapping("/shipment-item-delivery-plan/{deliveryPlanId}/all")
    public ResponseEntity<?> getAllOrderShipmentItemDeliveryPlan(
        Principal principal,
        @PathVariable String deliveryPlanId
    ) {
        log.info("::getOrderShipmentItem deliveryPlanId=" + deliveryPlanId);
        UserLogin userLogin = userService.findById(principal.getName());
        return ResponseEntity.ok().body(shipmentItemService.findAllInDeliveryPlan(deliveryPlanId, userLogin));
    }

    @GetMapping("/shipment-item-delivery-trip/{deliveryTripId}/all")
    public ResponseEntity<?> getAllOrderShipmentItemDeliveryTrip(
        Principal principal,
        @PathVariable String deliveryTripId
    ) {
        log.info("::getOrderShipmentItem deliveryTripId=" + deliveryTripId);
        UserLogin userLogin = userService.findById(principal.getName());

        return ResponseEntity
            .ok()
            .body(shipmentItemService.findAllInDeliveryPlanNearestDeliveryTrip(deliveryTripId, userLogin));
    }

    @GetMapping("/shipment-item-not-in-delivery-plan/{deliveryPlanId}/page")
    public ResponseEntity<?> getAllOrderShipmentItemNotInDeliveryPlan(
        Principal principal,
        @PathVariable String deliveryPlanId,
        Pageable pageable
    ) {
        log.info("::getAllOrderShipmentItemNotIn deliveryPlanId=" + deliveryPlanId);
        UserLogin userLogin = userService.findById(principal.getName());
        return ResponseEntity
            .ok()
            .body(shipmentItemService.findAllNotInDeliveryPlan(deliveryPlanId, pageable, userLogin));
    }

    @GetMapping("/shipment-item-not-in-delivery-plan/{deliveryPlanId}/all")
    public ResponseEntity<?> getOrderShipmentItemPageNotInDeliveryPlan(
        Principal principal,
        @PathVariable String deliveryPlanId
    ) {
        log.info("::getOrderShipmentItemPageNotIn deliveryPlanId=" + deliveryPlanId);
        UserLogin userLogin = userService.findById(principal.getName());
        return ResponseEntity.ok().body(shipmentItemService.findAllNotInDeliveryPlan(deliveryPlanId, userLogin));
    }

    @GetMapping("/shipment-item-of-user-login-not-in-delivery-plan/{deliveryPlanId}/all")
    public ResponseEntity<?> getOrderShipmentItemOfUserLoginPageNotInDeliveryPlan(
        Principal principal,
        @PathVariable String deliveryPlanId
    ) {
        UserLogin userLogin = userService.findById(principal.getName());

        log.info("::getOrderShipmentItemPageNotIn deliveryPlanId=" + deliveryPlanId);
        //return ResponseEntity.ok().body(shipmentItemService.findAllNotInDeliveryPlan(deliveryPlanId));
        return ResponseEntity
            .ok()
            .body(shipmentItemService.findAllByUserLoginNotInDeliveryPlan(userLogin, deliveryPlanId));
    }


    @PostMapping("/delete-shipment-item-delivery-plan")
    public ResponseEntity<?> deleteShipmentItemDeliveryPlan(
        Principal principal,
        @RequestBody ShipmentItemModel.DeleteDeliveryPlan deleteVehicleDeliveryPlanModel
    ) {
        log.info("::deleteShipmentItemDeliveryPlan: " + deleteVehicleDeliveryPlanModel.getDeliveryPlanId());
        return ResponseEntity
            .ok()
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
        return ResponseEntity.ok().body(deliveryPlanService.findById(deliveryPlanId));
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
        return ResponseEntity.ok().body(deliveryTripService.findById(deliveryTripId));
    }

    @GetMapping("/delivery-trip-detail/{deliveryTripId}")
    public ResponseEntity<?> getDeliveryTripDetailList(@PathVariable String deliveryTripId) {
        log.info("getDeliveryTripDetailList, deliveryTripId=" + deliveryTripId);
        return ResponseEntity.ok().body(deliveryTripDetailService.findAll(deliveryTripId));
    }

    @PostMapping("/create-delivery-trip")
    public ResponseEntity<?> createDeliveryTrip(
        Principal principal,
        @RequestBody com.hust.baseweb.applications.tms.model.DeliveryTripModel.Create input
    ) {
        log.info("::createDeliveryTrip: " + input);
        DeliveryTrip deliveryTrip = deliveryTripService.save(input, 0, 0, 0, 0, 0, 0, 0);
        return ResponseEntity.ok().body(deliveryTrip);
    }

    @PostMapping("/create-delivery-trip-detail/{deliveryTripId}")
    public ResponseEntity<?> createDeliveryTripDetail(
        Principal principal,
        @RequestBody List<DeliveryTripDetailModel.Create> inputs,
        @PathVariable String deliveryTripId
    ) {
        log.info("::createDeliveryTripDetail: " + deliveryTripId);

        UserLogin userLogin = userService.findById(principal.getName());

        return ResponseEntity.ok().body(deliveryTripDetailService.save(deliveryTripId, inputs, userLogin));
    }

    @GetMapping("/delete-delivery-trip-detail/{deliveryTripDetailId}")
    public ResponseEntity<?> deleteDeliveryTripDetail(
        Principal principal,
        @PathVariable String deliveryTripDetailId
    ) {
        log.info("::deleteDeliveryTripDetail: " + deliveryTripDetailId);
        UserLogin userLogin = userService.findById(principal.getName());
        return ResponseEntity.ok().body(deliveryTripDetailService.delete(deliveryTripDetailId, userLogin));
    }

    @PostMapping("/create-shipment-item-delivery-plan")
    public ResponseEntity<?> createShipmentItemDeliveryPlan(
        Principal principal,
        @RequestBody ShipmentItemModel.CreateDeliveryPlan createDeliveryPlan
    ) {
        log.info("::createShipmentItemDeliveryPlan: " + createDeliveryPlan.getDeliveryPlanId());

        UserLogin userLogin = userService.findById(principal.getName());

        return ResponseEntity
            .ok()
            .body(shipmentItemService.saveShipmentItemDeliveryPlan(createDeliveryPlan, userLogin));
    }

    @PostMapping("/delivery-trip/{deliveryTripId}/capacity-info")
    public ResponseEntity<?> getDeliveryTripCapacityInfo(
        @PathVariable String deliveryTripId,
        @RequestBody List<DeliveryTripDetailModel.Create> shipmentItemModels,
        Principal principal
    ) {
        log.info("::getDeliveryTripCapacityInfo(): deliveryTripId=" + deliveryTripId);
        UserLogin userLogin = userService.findById(principal.getName());
        return ResponseEntity
            .ok()
            .body(deliveryTripService.getDeliveryTripInfo(deliveryTripId, shipmentItemModels, userLogin));
    }

    @PostMapping("/delivery-trips/chart-info")
    public ResponseEntity<?> getDeliveryTripsChartInfo(@RequestBody List<String> deliveryTripIds, Principal principal) {
        log.info("::getDeliveryTripsChartInfo(), deliveryTripIdsSize=" + deliveryTripIds.size());

        UserLogin userLogin = userService.findById(principal.getName());

        List<DeliveryTripModel> deliveryTripModels = new ArrayList<>();

        for (String deliveryTripId : deliveryTripIds) {
            DeliveryTripModel deliveryTripModel = deliveryTripService.findById(deliveryTripId);
            DeliveryTripModel.Tour tour = deliveryTripService.getDeliveryTripInfo(
                deliveryTripId,
                new ArrayList<>(), userLogin);
            deliveryTripModel.setTotalDistance(tour.getTotalDistance());
            deliveryTripModel.setTotalWeight(tour.getTotalWeight());
            deliveryTripModels.add(deliveryTripModel);
        }

        return ResponseEntity.ok(deliveryTripModels);
    }


    @GetMapping("/approve-delivery-trip/{deliveryTripId}")
    public ResponseEntity<?> approveDeliveryTrip(@PathVariable String deliveryTripId) {
        return ResponseEntity.ok().body(deliveryTripService.approveDeliveryTrip(deliveryTripId));
    }

    @GetMapping("/start-execute-delivery-trip/{deliveryTripId}")
    public ResponseEntity<?> startExecuteDeliveryTrip(@PathVariable String deliveryTripId) {
        return ResponseEntity.ok().body(deliveryTripService.startExecuteDeliveryTrip(deliveryTripId));
    }

    @GetMapping("/complete-delivery-trip-detail/{deliveryTripDetailId}")
    public ResponseEntity<?> completeDeliveryTripDetail(@PathVariable String deliveryTripDetailId) {
        log.info("completeDeliveryTrip({})", deliveryTripDetailId);
        return ResponseEntity
            .ok()
            .body(deliveryTripDetailService.completeDeliveryTripDetail(deliveryTripDetailId));
    }

    @PostMapping("/complete-delivery-trip-details")
    public ResponseEntity<?> completeDeliveryTripDetail(@RequestBody List<String> deliveryTripDetailIds) {
        log.info("completeDeliveryTrip({})", deliveryTripDetailIds.size());
        return ResponseEntity
            .ok()
            .body(deliveryTripDetailService.completeDeliveryTripDetail(deliveryTripDetailIds.toArray(new String[0])));
    }

    @GetMapping("/shipment-item-not-scheduled/{deliveryPlanId}")
    public ResponseEntity<?> getShipmentItemNotScheduled(@PathVariable String deliveryPlanId, Principal principal) {
        UserLogin userLogin = userService.findById(principal.getName());

        return ResponseEntity.ok(shipmentItemService.findAllNotScheduled(deliveryPlanId, userLogin));
    }

    @GetMapping("/shipment-item-info/{shipmentItemId}")
    public ResponseEntity<?> getShipmentItemInfo(@PathVariable String shipmentItemId) {
        return ResponseEntity.ok(shipmentItemService.getShipmentItemInfo(shipmentItemId));
    }

    @GetMapping("/get-total-weight-shipment-items-in-delivery-plan/{deliveryPlanId}")
    public ResponseEntity<?> getTotalWeightShipmentItemsInDeliveryPlan(@PathVariable String deliveryPlanId) {
        return ResponseEntity.ok(deliveryPlanService.getTotalWeightShipmentItems(deliveryPlanId));
    }

    @GetMapping("/delete-all-delivery-trips")
    public ResponseEntity<?> deleteAllDeliveryTrips() {
        log.info("deleteAllDeliveryTrips()");
        return ResponseEntity.ok(deliveryTripService.deleteAll());
    }
}

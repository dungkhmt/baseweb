package com.hust.baseweb.applications.tms.controller;

import java.security.Principal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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
import com.hust.baseweb.rest.user.UserController;

@RestController
@CrossOrigin
public class ShipmentOrderAPIController {
	public static Logger LOG = LoggerFactory.getLogger(ShipmentOrderAPIController.class);
	
	@Autowired
	private ShipmentService shipmentService;
	
	@Autowired
	private DeliveryPlanService deliveryPlanService;
	
	@Autowired
	private DeliveryTripService deliveryTripService;
	
	@Autowired
	private DeliveryTripDetailService deliveryTripDetailService;
	
	@PostMapping("/create-shipment")
	public ResponseEntity<?> createOrderShipment(Principal principal, @RequestBody CreateShipmentInputModel input){
		LOG.info("::createOrderShipment, shipment-items = " + input.getShipmentItems().length);
		
		Shipment shipment = shipmentService.save(input);
		return ResponseEntity.ok().body(shipment);
	}
	
	@PostMapping("/create-delivery-plan")
	public ResponseEntity<?> createDeliveryPlan(Principal principal, @RequestBody CreateDeliveryPlanInputModel input){
		LOG.info("createDeliveryPlan....");
		
		DeliveryPlan deliveryPlan = deliveryPlanService.save(input);
		
		return ResponseEntity.ok().body(deliveryPlan);
	}
	
	@PostMapping("/create-delivery-trip")
	public ResponseEntity<?> createDeliveryTrip(Principal principal, @RequestBody CreateDeliveryTripInputModel input){
		DeliveryTrip deliveryTrip = deliveryTripService.save(input);
		return ResponseEntity.ok().body(deliveryTrip);
	}
	
	@PostMapping("/create-delivery-trip-detail")
	public ResponseEntity<?> createDeliveryTripDetail(Principal principal, @RequestBody CreateDeliveryTripDetailInputModel input){
		DeliveryTripDetail deliveryTripDetail = null;
		deliveryTripDetail = deliveryTripDetailService.save(input);
		return ResponseEntity.ok().body(deliveryTripDetail);
	}
}

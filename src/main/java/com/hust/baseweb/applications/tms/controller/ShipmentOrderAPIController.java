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

import com.hust.baseweb.applications.tms.entity.Shipment;
import com.hust.baseweb.applications.tms.model.shipmentorder.CreateShipmentInputModel;
import com.hust.baseweb.applications.tms.service.ShipmentService;
import com.hust.baseweb.rest.user.UserController;

@RestController
@CrossOrigin
public class ShipmentOrderAPIController {
	public static Logger LOG = LoggerFactory.getLogger(ShipmentOrderAPIController.class);
	
	@Autowired
	private ShipmentService shipmentService;
	
	@PostMapping("/create-shipment")
	public ResponseEntity<?> createOrderShipment(Principal principal, @RequestBody CreateShipmentInputModel input){
		LOG.info("::createOrderShipment, shipment-items = " + input.getShipmentItems().length);
		
		Shipment shipment = shipmentService.save(input);
		return ResponseEntity.ok().body(shipment);
	}
}

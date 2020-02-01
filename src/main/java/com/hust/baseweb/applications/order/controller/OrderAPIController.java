package com.hust.baseweb.applications.order.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.hust.baseweb.applications.order.entity.OrderHeader;
import com.hust.baseweb.applications.order.model.ModelCreateOrderInput;
import com.hust.baseweb.applications.order.service.OrderService;

import java.security.Principal;

@RestController
@CrossOrigin

public class OrderAPIController {
	public static final String module = OrderAPIController.class.getName();
	
	@Autowired
	private OrderService orderService;
	
	@PostMapping("/create-order")
	public ResponseEntity createOrder(Principal principal, @RequestBody ModelCreateOrderInput input){
		//TODO
		OrderHeader order = orderService.save(input);
		
		return ResponseEntity.ok().body(order);
		
	}
}

package com.hust.baseweb.applications.order.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.hust.baseweb.applications.order.model.ModelCreateOrderInput;

import java.security.Principal;

@RestController
@CrossOrigin

public class OrderAPIController {
	public static final String module = OrderAPIController.class.getName();
	
	@PostMapping("/create-order")
	public ResponseEntity createOrder(Principal principal, @RequestBody ModelCreateOrderInput input){
		//TODO
		return null;
		
	}
}

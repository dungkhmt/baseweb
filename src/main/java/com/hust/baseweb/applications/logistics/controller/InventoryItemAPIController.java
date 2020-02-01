package com.hust.baseweb.applications.logistics.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.hust.baseweb.applications.logistics.model.ImportInventoryItemInputModel;
import com.hust.baseweb.applications.logistics.model.ImportInventoryItemsInputModel;
import com.hust.baseweb.applications.logistics.service.InventoryItemService;

import java.security.Principal;

@RestController
@CrossOrigin
public class InventoryItemAPIController {
	public static final String module = InventoryItemAPIController.class.getName();
	
	@Autowired
	InventoryItemService inventoryItemService;
	
	@PostMapping("/import-inventory-items")
	public ResponseEntity importInventoryItems(Principal principal, @RequestBody ImportInventoryItemsInputModel input){
		System.out.println(module + "::importInventoryItems, input.sz = " + input.getInventoryItems().length);
		
		for(ImportInventoryItemInputModel i: input.getInventoryItems()){
			inventoryItemService.save(i);
		}
		return ResponseEntity.ok().body("ok");
	}
}

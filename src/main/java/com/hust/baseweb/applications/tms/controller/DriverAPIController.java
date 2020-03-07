package com.hust.baseweb.applications.tms.controller;

import com.hust.baseweb.applications.tms.entity.PartyDriver;
import com.hust.baseweb.applications.tms.model.driver.CreateDriverInputModel;
import com.hust.baseweb.applications.tms.model.driver.FindAllDriversInputModel;
import com.hust.baseweb.applications.tms.service.PartyDriverService;
import com.hust.baseweb.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;



@RestController
@CrossOrigin
@AllArgsConstructor(onConstructor = @__(@Autowired))
@Log4j2
public class DriverAPIController {

	private PartyDriverService partyDriverService;
	private UserService userService;
	
	@PostMapping("/create-driver")
	public ResponseEntity<?> createDriver(Principal principal, @RequestBody CreateDriverInputModel input){
		PartyDriver driver = partyDriverService.save(input);
		return ResponseEntity.ok().body(driver);
	}
	
	@PostMapping("/get-all-drivers")
	public ResponseEntity<?> findAllDrivers(Principal principal, @RequestBody FindAllDriversInputModel input){
		List<PartyDriver> drivers = partyDriverService.findAll();
		return ResponseEntity.ok().body(drivers);
	}
}

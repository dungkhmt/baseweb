package com.hust.baseweb.applications.humanresource.controller;

import java.security.Principal;
import java.util.List;

import com.hust.baseweb.applications.humanresource.entity.PartyDepartment;
import com.hust.baseweb.applications.humanresource.model.AddParty2DepartmentInputModel;
import com.hust.baseweb.applications.humanresource.service.PartyDepartmentService;
import com.hust.baseweb.entity.UserLogin;
import com.hust.baseweb.service.UserService;
import lombok.extern.log4j.Log4j2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.hust.baseweb.applications.humanresource.entity.Department;
import com.hust.baseweb.applications.humanresource.model.CreateDepartmentInputModel;
import com.hust.baseweb.applications.humanresource.service.DepartmentService;

@RestController
@Log4j2
public class DepartmentAPIController {

	@Autowired
	private DepartmentService departmentService;
	@Autowired
	private UserService userService;

	@Autowired
	private PartyDepartmentService partyDepartmentService;

	@PostMapping("/create-department")
	public ResponseEntity<?> createDepartment(Principal principal, @RequestBody CreateDepartmentInputModel input){
		log.info("createDepartment, departmentName = " + input.getDepartmentName());
		Department dept = departmentService.save(input.getDepartmentName());
		
		return ResponseEntity.ok().body(dept);
	}
	
	@GetMapping("/get-all-departments")
	public ResponseEntity<?> getAllDeparmtents(Principal principal){
		log.info("getAllDeparmtents...");
		List<Department> depts = departmentService.findAll();
		return ResponseEntity.ok().body(depts);
		
	}

	@PostMapping("/add-user-2-department")
	public ResponseEntity<?> addUser2Department(Principal principal, @RequestBody AddParty2DepartmentInputModel input){
		UserLogin userLogin = userService.findById(principal.getName());
		log.info("addUser2Department, addUser2Department, user login = " + userLogin.getUserLoginId());

		PartyDepartment partyDepartment = partyDepartmentService.save(input.getPartyId(),
				input.getDepartmentId(), input.getRoleTypeId());

		return ResponseEntity.ok().body("add OK");
	}

}

package com.hust.baseweb.applications.humanresource.controller;

import java.security.Principal;
import java.util.List;

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
	
}

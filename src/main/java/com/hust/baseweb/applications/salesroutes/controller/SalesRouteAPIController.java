package com.hust.baseweb.applications.salesroutes.controller;

import java.security.Principal;

import lombok.extern.log4j.Log4j2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.hust.baseweb.applications.salesroutes.entity.SalesRouteConfig;
import com.hust.baseweb.applications.salesroutes.entity.SalesRouteConfigCustomer;
import com.hust.baseweb.applications.salesroutes.entity.SalesRoutePlanningPeriod;
import com.hust.baseweb.applications.salesroutes.model.salesrouteconfig.CreateSalesRouteConfigInputModel;
import com.hust.baseweb.applications.salesroutes.model.salesrouteconfigcustomer.CreateSalesRouteConfigCustomerInputModel;
import com.hust.baseweb.applications.salesroutes.model.salesroutedetail.GenerateSalesRouteDetailInputModel;
import com.hust.baseweb.applications.salesroutes.model.salesrouteplanningperiod.CreateSalesRoutePlanningPeriodInputModel;
import com.hust.baseweb.applications.salesroutes.service.SalesRouteConfigCustomerService;
import com.hust.baseweb.applications.salesroutes.service.SalesRouteConfigService;
import com.hust.baseweb.applications.salesroutes.service.SalesRoutePlanningPeriodService;

@RestController
@CrossOrigin
@Log4j2
public class SalesRouteAPIController {

	@Autowired
	private SalesRouteConfigService salesRouteConfigService;
	
	@Autowired
	private SalesRouteConfigCustomerService salesRouteConfigCustomerService;
	
	@Autowired
	private SalesRoutePlanningPeriodService salesRoutePlanningPeriodService;
	
	@PostMapping("/create-sales-route-config")
	public ResponseEntity<?> createSalesRouteConfig(Principal principal,
			@RequestBody CreateSalesRouteConfigInputModel input) {
		log.info("createSalesRouteConfig, days = " + input.getDays() + ", repeatWeek = " + input.getRepeatWeek());
		
		SalesRouteConfig salesRouteConfig = salesRouteConfigService.save(input.getDays(), input.getRepeatWeek());
		
		return ResponseEntity.ok().body(salesRouteConfig);
	}
	
	@PostMapping("/create-sales-route-config-customer")
	public ResponseEntity<?> createSalesRouteConfigCustomer(Principal principal, @RequestBody CreateSalesRouteConfigCustomerInputModel input){
		log.info("createSalesRouteConfigCustomer, salesRouteConfigId = " + input.getSalesRouteConfigId() + ", customerId = " + input.getPartyCustomerId()
				+ ", salesmanId = " + input.getPartySalesmanId() + ", startExecuteDate = " + input.getStartExecuteDate());
		
		SalesRouteConfigCustomer salesRouteConfigCustomer = salesRouteConfigCustomerService.save(input.getSalesRouteConfigId(), 
				input.getPartyCustomerId(), input.getPartySalesmanId(), input.getStartExecuteDate());
		
		return ResponseEntity.ok().body(salesRouteConfigCustomer);
	}
	
	@PostMapping("/create-sales-route-planning-period")
	public ResponseEntity<?> createSalesRoutePlanningPeriod(Principal principal, @RequestBody CreateSalesRoutePlanningPeriodInputModel input){
		log.info("createSalesRoutePlanningPeriod, fromDate = " + input.getFromDate() + ", toDate = " + input.getToDate() + ", description = " +  input.getDescription());
		SalesRoutePlanningPeriod salesRoutePlanningPeriod = salesRoutePlanningPeriodService.save(input.getFromDate(), input.getToDate(), input.getDescription());
		
		return ResponseEntity.ok().body(salesRoutePlanningPeriod);
		
	}
	
	@PostMapping("/generate-sales-route-detail")
	public ResponseEntity<?> generateSalesRouteDetail(Principal principal, @RequestBody GenerateSalesRouteDetailInputModel input){
		log.info("generateSalesRouteDetail, salesmanId = " + input.getPartySalesmanId());
		
		return ResponseEntity.ok().body("ok");
	}
}

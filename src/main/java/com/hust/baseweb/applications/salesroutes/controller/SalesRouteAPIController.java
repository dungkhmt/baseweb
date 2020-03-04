package com.hust.baseweb.applications.salesroutes.controller;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

import lombok.extern.log4j.Log4j2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hust.baseweb.applications.customer.entity.PartyCustomer;
import com.hust.baseweb.applications.sales.model.customersalesman.GetCustomersOfSalesmanInputModel;
import com.hust.baseweb.applications.salesroutes.entity.SalesRouteConfig;
import com.hust.baseweb.applications.salesroutes.entity.SalesRouteConfigCustomer;
import com.hust.baseweb.applications.salesroutes.entity.SalesRoutePlanningPeriod;
import com.hust.baseweb.applications.salesroutes.entity.SalesmanCheckinHistory;
import com.hust.baseweb.applications.salesroutes.model.salesmancheckinout.SalesmanCheckInOutInputModel;
import com.hust.baseweb.applications.salesroutes.model.salesrouteconfig.CreateSalesRouteConfigInputModel;
import com.hust.baseweb.applications.salesroutes.model.salesrouteconfigcustomer.CreateSalesRouteConfigCustomerInputModel;
import com.hust.baseweb.applications.salesroutes.model.salesroutedetail.GenerateSalesRouteDetailInputModel;
import com.hust.baseweb.applications.salesroutes.model.salesroutedetail.GetCustomersVisitedBySalesmanDayInputModel;
import com.hust.baseweb.applications.salesroutes.model.salesroutedetail.GetCustomersVisitedDayOfUserLogin;
import com.hust.baseweb.applications.salesroutes.model.salesrouteplanningperiod.CreateSalesRoutePlanningPeriodInputModel;
import com.hust.baseweb.applications.salesroutes.service.SalesRouteConfigCustomerService;
import com.hust.baseweb.applications.salesroutes.service.SalesRouteConfigService;
import com.hust.baseweb.applications.salesroutes.service.SalesRouteDetailService;
import com.hust.baseweb.applications.salesroutes.service.SalesRoutePlanningPeriodService;
import com.hust.baseweb.applications.salesroutes.service.SalesmanCheckinHistoryService;
import com.hust.baseweb.entity.UserLogin;
import com.hust.baseweb.service.UserService;

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
	
	@Autowired
	private SalesRouteDetailService salesRouteDetailService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private SalesmanCheckinHistoryService salesmanCheckinService;
	
	@PostMapping("/salesman-checkin-customer")
	public ResponseEntity<?> salesmanCheckInCustomer(Principal principal, @RequestBody SalesmanCheckInOutInputModel input){
		UserLogin userLogin = userService.findById(principal.getName());
		SalesmanCheckinHistory sch = salesmanCheckinService.save(userLogin, input.getPartyCustomerId(), "Y", input.getLatitude() + "," + input.getLongitude());
		return ResponseEntity.ok().body(sch);
	}
	@PostMapping("/salesman-checkout-customer")
	public ResponseEntity<?> salesmanCheckOutCustomer(Principal principal, @RequestBody SalesmanCheckInOutInputModel input){
		UserLogin userLogin = userService.findById(principal.getName());
		SalesmanCheckinHistory sch = salesmanCheckinService.save(userLogin, input.getPartyCustomerId(), "N", input.getLatitude() + "," + input.getLongitude());
		return ResponseEntity.ok().body(sch);
	}
	@GetMapping("/salesman-checkin-history")
    public ResponseEntity<?> getSalesmanCheckInHistory(Principal principal, Pageable page, @RequestParam(required = false) String param){
		UserLogin userLogin = userService.findById(principal.getName());
		
		log.info("getSalesmanCheckInHistory, user = " + userLogin.getUserLoginId());
		
		Page<SalesmanCheckinHistory> list = salesmanCheckinService.findAll(page);
		return ResponseEntity.ok().body(list);
	}
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
		int cnt = salesRouteDetailService.generateSalesRouteDetailOfSalesman(input.getPartySalesmanId(), input.getSalesRoutePlanningPeriodId());
		return ResponseEntity.ok().body(cnt);
	}
	
	@PostMapping("/get-customers-visited-salesman-date")
	public ResponseEntity<?> getCustomersVisitedSalesmanDay(Principal principal, @RequestBody GetCustomersVisitedBySalesmanDayInputModel input){
		List<PartyCustomer> customers = salesRouteDetailService.getCustomersVisitedSalesmanDay(input.getPartySalesmanId(), input.getDate());
		return ResponseEntity.ok().body(customers);
	}
	@PostMapping("/get-customers-visited-date-of-userlogin")
	public ResponseEntity<?> getCustomersVisitedDateOfUserLogin(Principal principal, @RequestBody GetCustomersVisitedDayOfUserLogin input){
		UserLogin userLogin = userService.findById(principal.getName());
		UUID partySalesmanId = userLogin.getParty().getPartyId();
		log.info("getCustomersVisitedDateOfUserLogin, partySalesmanId = " + partySalesmanId + ", date = " + input.getDate());
		List<PartyCustomer> customers = salesRouteDetailService.getCustomersVisitedSalesmanDay(partySalesmanId, input.getDate());
		return ResponseEntity.ok().body(customers);
	}
}

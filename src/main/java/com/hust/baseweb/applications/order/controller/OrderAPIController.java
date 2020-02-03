package com.hust.baseweb.applications.order.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.hust.baseweb.applications.order.cache.RevenueOrderCache;
import com.hust.baseweb.applications.order.entity.OrderHeader;
import com.hust.baseweb.applications.order.entity.PartyCustomer;
import com.hust.baseweb.applications.order.model.GetListOrdersInputModel;
import com.hust.baseweb.applications.order.model.GetListPartyCustomerInputModel;
import com.hust.baseweb.applications.order.model.GetListPartyCustomerOutputModel;
import com.hust.baseweb.applications.order.model.GetListSalesmanInputModel;
import com.hust.baseweb.applications.order.model.GetOrderDetailInputModel;
import com.hust.baseweb.applications.order.model.GetTotalRevenueInputModel;
import com.hust.baseweb.applications.order.model.GetTotalRevenueItemOutputModel;
import com.hust.baseweb.applications.order.model.GetTotalRevenueOutputModel;
import com.hust.baseweb.applications.order.model.ModelCreateOrderInput;
import com.hust.baseweb.applications.order.service.OrderService;
import com.hust.baseweb.applications.order.service.PartyCustomerService;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

@RestController
@CrossOrigin

public class OrderAPIController {
	public static final String module = OrderAPIController.class.getName();
	
	public static RevenueOrderCache revenueOrderCache = new RevenueOrderCache();
	
	@Autowired
	private OrderService orderService;
	
	@Autowired
	private PartyCustomerService partyCustomerService;
	
	@PostMapping("/create-order")
	public ResponseEntity createOrder(Principal principal, @RequestBody ModelCreateOrderInput input){
		//TODO
		OrderHeader order = orderService.save(input);
		
		return ResponseEntity.ok().body(order);		
	}
	@PostMapping("/get-order-detail")
	public ResponseEntity getOrderDetail(Principal principal, @RequestBody GetOrderDetailInputModel input){
		// TODO
		return null;
	}
	
	@PostMapping("/get-list-orders")
	public ResponseEntity getListOrders(Principal principal, @RequestBody GetListOrdersInputModel input){
		// TODO
		return null;
	}
	
	@PostMapping("/get-list-party-customers")
	public ResponseEntity getListPartyCustomers(Principal principal, @RequestBody GetListPartyCustomerInputModel input){
		// TODO
		List<PartyCustomer> lst = partyCustomerService.getListPartyCustomers();
		return ResponseEntity.ok().body(new GetListPartyCustomerOutputModel(lst));
	}
	@PostMapping("/get-list-salesmans")
	public ResponseEntity getListSalesmans(Principal principal, @RequestBody GetListSalesmanInputModel input){
		// TODO
		return null;
	}
	
	@PostMapping("/get-total-revenue")
	public ResponseEntity getTotalRevenue(Principal principal, @RequestBody GetTotalRevenueInputModel input){
		
		Enumeration<String> keys = OrderAPIController.revenueOrderCache.keys();
		List<String> l_keys = new ArrayList<String>();
		while(keys.hasMoreElements()){
			l_keys.add(keys.nextElement());
		}
		GetTotalRevenueItemOutputModel[] lst = new GetTotalRevenueItemOutputModel[l_keys.size()];
		for(int i = 0; i < lst.length; i++){
			lst[i] = new GetTotalRevenueItemOutputModel(l_keys.get(i), OrderAPIController.revenueOrderCache.getRevenue(l_keys.get(i)));
		}
		return ResponseEntity.ok().body(new GetTotalRevenueOutputModel(lst));
	}
}

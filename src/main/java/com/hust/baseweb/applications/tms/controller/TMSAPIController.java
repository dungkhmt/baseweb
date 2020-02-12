package com.hust.baseweb.applications.tms.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hust.baseweb.applications.customer.entity.PartyCustomer;
import com.hust.baseweb.applications.customer.repo.CustomerRepo;
import com.hust.baseweb.applications.order.entity.OrderHeader;
import com.hust.baseweb.applications.order.entity.OrderItem;
import com.hust.baseweb.applications.order.entity.OrderRole;
import com.hust.baseweb.applications.order.repo.OrderRepo;
import com.hust.baseweb.applications.order.repo.OrderRoleRepo;
import com.hust.baseweb.applications.tms.model.deliveryrouteofshipper.DeliveryCustomerModel;
import com.hust.baseweb.applications.tms.model.deliveryrouteofshipper.DeliveryItemModel;
import com.hust.baseweb.applications.tms.model.deliveryrouteofshipper.GetAssigned2ShipperDeliveryRouteOutputModel;
import com.hust.baseweb.entity.UserLogin;
import com.hust.baseweb.repo.UserLoginRepo;

@RestController
@CrossOrigin
public class TMSAPIController {
	public static final String module = TMSAPIController.class.getName();
	
	@Autowired
	private CustomerRepo customerRepo;
	
	@Autowired
	private OrderRepo orderRepo;
	@Autowired
	private OrderRoleRepo orderRoleRepo;
	@Autowired
	private UserLoginRepo userLoginRepo;
	
	@GetMapping("/get-assigned-delivery-routes")
	private ResponseEntity<?> getAssignedDeliveryRoutes(Principal principal){
		System.out.println(module + "::getAssignedDeliveryRoutes, user = " + principal.getName());
		UserLogin userLogin = userLoginRepo.findByUserLoginId(principal.getName());
		List<PartyCustomer> customers = customerRepo.findAll();
		List<OrderHeader> orders = orderRepo.findAll();
		List<OrderRole> orderRoles = orderRoleRepo.findAll();
		//List<OrderHeader> sel_orders = new ArrayList<OrderHeader>();
		//for(int i = 0; i < 3; i++)
		//	sel_orders.add(orders.get(i));
		System.out.println(module + "::getAssignedDeliveryRoutes, orders.sz = " + orders.size() + ", orderRoles.sz = " + orderRoles.size() + ", customers = " + customers.size());
		HashMap<String, OrderHeader> mID2Order = new HashMap<String, OrderHeader>();
		for(OrderHeader o: orders){
			mID2Order.put(o.getOrderId(), o);
		}
		List<DeliveryCustomerModel> deliveryCustomers = new ArrayList<DeliveryCustomerModel>();
		for(int i = 0; i < customers.size(); i++){
			PartyCustomer c = customers.get(i);
			if(c.getPostalAddress() == null || c.getPostalAddress().size() == 0) continue;
			//if(!c.getPartyId().equals(userLogin.getParty().getPartyId())) continue;
			List<OrderHeader> orderOfCustomer = new ArrayList<OrderHeader>();
			for(OrderRole or: orderRoles){
				if(or.getPartyId().toString().equals(c.getPartyId().toString())){
					String orderId = or.getOrderId();
					//for(OrderHeader o: orders) if(o.getOrderId().equals(orderId))
					//	orderOfCustomer.add(o);
					orderOfCustomer.add(mID2Order.get(orderId));
					if(orderOfCustomer.size() > 1) break;// FOR TESTING
				}
			}
			System.out.println(module + "::::getAssignedDeliveryRoutes, find " + orderOfCustomer.size() + " orders for customers " + c.getCustomerName());
			
			List<DeliveryItemModel> lst = new ArrayList<DeliveryItemModel>();
			for(OrderHeader o: orderOfCustomer){
				for(OrderItem oi: o.getOrderItems()){
					lst.add(new DeliveryItemModel(oi.getOrderId(),oi.getOrderItemSeqId(),oi.getProduct().getProductId(),oi.getQuantity()));
				}
			}
			deliveryCustomers.add(new DeliveryCustomerModel(c, lst));
		}
		
		return ResponseEntity.ok().body(new GetAssigned2ShipperDeliveryRouteOutputModel(deliveryCustomers));
	}
}

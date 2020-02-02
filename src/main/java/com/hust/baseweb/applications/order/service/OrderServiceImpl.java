package com.hust.baseweb.applications.order.service;



import java.math.BigDecimal;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hust.baseweb.applications.logistics.entity.Facility;
import com.hust.baseweb.applications.logistics.entity.Product;
import com.hust.baseweb.applications.logistics.repo.FacilityRepo;
import com.hust.baseweb.applications.logistics.repo.ProductRepo;
import com.hust.baseweb.applications.order.entity.OrderHeader;
import com.hust.baseweb.applications.order.entity.OrderItem;
import com.hust.baseweb.applications.order.entity.OrderRole;
import com.hust.baseweb.applications.order.entity.OrderStatus;
import com.hust.baseweb.applications.order.entity.OrderType;
import com.hust.baseweb.applications.order.entity.SalesChannel;
import com.hust.baseweb.applications.order.model.ModelCreateOrderInput;
import com.hust.baseweb.applications.order.model.ModelCreateOrderInputOrderItem;
import com.hust.baseweb.applications.order.repo.OrderItemRepo;
import com.hust.baseweb.applications.order.repo.OrderRepo;
import com.hust.baseweb.applications.order.repo.OrderRoleRepo;
import com.hust.baseweb.applications.order.repo.OrderStatusRepo;
import com.hust.baseweb.applications.order.repo.OrderTypeRepo;
import com.hust.baseweb.applications.order.repo.SalesChannelRepo;
import com.hust.baseweb.entity.UserLogin;
import com.hust.baseweb.repo.UserLoginRepo;

@Service
public class OrderServiceImpl implements OrderService {
	public static final String module = OrderServiceImpl.class.getName();
	
	@Autowired
	private UserLoginRepo userLoginRepo;
	
	@Autowired
	private OrderRepo orderRepo;
	
	@Autowired
	private OrderItemRepo orderItemRepo;
	
	@Autowired
	private OrderRoleRepo orderRoleRepo;
	
	@Autowired
	private OrderStatusRepo orderStatusRepo;
	
	@Autowired
	private OrderTypeRepo orderTypeRepo;
	
	@Autowired
	private ProductRepo productRepo;
	
	@Autowired
	private FacilityRepo facilityRepo;
	
	@Autowired
	private SalesChannelRepo salesChannelRepo;
	
	@Override
	@Transactional
	public OrderHeader save(ModelCreateOrderInput orderInput) {
		// TODO Auto-generated method stub
		
		OrderType orderType = orderTypeRepo.findByOrderTypeId("SALES_ORDER");
		SalesChannel salesChannel = salesChannelRepo.findBySalesChannelId(orderInput.getSalesChannelId());
		String salesmanId = orderInput.getSalesmanId();
		System.out.println(module + "::save, salesmanId = " + salesmanId);
		UserLogin salesman = userLoginRepo.findByUserLoginId(salesmanId);
		Facility facility = facilityRepo.findByFacilityId(orderInput.getFacilityId());
		
		UUID uuid = UUID.randomUUID();
        String orderId = uuid.toString();
        
		System.out.println(module + "::save, orderId = " + orderId + ", sales channel = " + 
		(salesChannel != null ? salesChannel.getSalesChannelName() : "null") + ", userLogin = " + 
				(salesman != null ? salesman.getUserLoginId(): "null") + ", facility = " + 
		(facility != null ? facility.getFacilityName() : "null"));
		
		
        
		OrderHeader order = new OrderHeader();
		order.setOrderId(orderId);
		order.setOrderType(orderType);
		order.setSalesChannel(salesChannel);
		order.setFacility(facility);
		
		orderRepo.save(order);
		
		// write to table order_item
		int idx = 0;
		for(ModelCreateOrderInputOrderItem oi: orderInput.getOrderItems()){
			idx++;
			String orderItemSeqId = "0000" + idx;
			Product product = productRepo.findByProductId(oi.getProductId());
			OrderItem orderItem = new OrderItem();
			orderItem.setOrderId(order.getOrderId());
			orderItem.setOrderItemSeqId(orderItemSeqId);
			orderItem.setProduct(product);
			orderItem.setQuantity(oi.getQuantity());
			orderItem.setUnitPrice(new BigDecimal(0));// TOBE FIXED
			
			orderItemRepo.save(orderItem);
		}
		
		// write to order_role
		OrderRole ol = new OrderRole();
		ol.setOrderId(order.getOrderId());
		ol.setPartyId(orderInput.getPartyCustomerId());
		ol.setRoleTypeId("BILL_TO_CUSTOMER");
		orderRoleRepo.save(ol);
		
		ol = new OrderRole();
		ol.setOrderId(order.getOrderId());
		ol.setPartyId(salesman.getParty().getPartyId());
		ol.setRoleTypeId("SALES_EXECUTIVE");// salesman who sales the order (revenue of the order is accounted for this salesman)
		orderRoleRepo.save(ol);
		
		// write to order-status
		OrderStatus os = new OrderStatus();
		String orderStatusId = UUID.randomUUID().toString();		
		os.setOrder(order);
		os.setOrderStatusId(orderStatusId);
		os.setStatusId("ORDER_CREATED"); 
		orderStatusRepo.save(os);
		return order;
	}

}

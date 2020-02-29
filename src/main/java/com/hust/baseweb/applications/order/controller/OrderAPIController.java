package com.hust.baseweb.applications.order.controller;

import com.google.gson.Gson;
import com.hust.baseweb.applications.customer.entity.PartyCustomer;
import com.hust.baseweb.applications.order.cache.RevenueOrderCache;
import com.hust.baseweb.applications.order.entity.OrderHeader;
import com.hust.baseweb.applications.order.model.*;
import com.hust.baseweb.applications.order.repo.POrderRepo;
import com.hust.baseweb.applications.order.service.OrderService;
import com.hust.baseweb.applications.order.service.PartyCustomerService;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Collections;
import java.util.List;

@RestController
@CrossOrigin
@AllArgsConstructor(onConstructor = @__(@Autowired))
@Log4j2
public class OrderAPIController {
    public static final String module = OrderAPIController.class.getName();

    public static RevenueOrderCache revenueOrderCache = new RevenueOrderCache();

    private OrderService orderService;
    private PartyCustomerService partyCustomerService;
    
    
    @PostMapping("/create-order-distributor-to-retailoutlet")
    public ResponseEntity createOrder(Principal principal, @RequestBody ModelCreateOrderInput input) {
        //TODO
    	Gson gson = new Gson();
    	String inputJson = gson.toJson(input);
    	log.info("createOrder, input json = " +  inputJson);
    	
    	
        OrderHeader order = orderService.save(input);

        return ResponseEntity.ok().body(order);
    }

    @PostMapping("/get-order-detail")
    public ResponseEntity getOrderDetail(Principal principal, @RequestBody GetOrderDetailInputModel input) {
        // TODO
        return null;
    }

    @PostMapping("/get-list-orders")
    public ResponseEntity getListOrders(Principal principal, @RequestBody GetListOrdersInputModel input) {
        // TODO
        return null;
    }
    @GetMapping("/orders")
    public ResponseEntity<?> getOrders(Pageable page, @RequestParam(required = false) String param){
    	log.info("getOrders, page = pageNumber = " + page.getPageNumber() + ", offSet = " +
                page.getOffset() + ", pageSize = " + page.getPageSize() + ", param = " + param);
    	Page<OrderHeader> orders = orderService.findAll(page);
    	return ResponseEntity.ok().body(orders);
    }
    @GetMapping(path = "/orders/{orderId}")
    public ResponseEntity<?> getOrderDetail(@PathVariable String orderId, Principal principal) {
    	log.info("getOrderDetail, orderId = " + orderId);
    	
    	OrderHeader order = orderService.findByOrderId(orderId);
    	return ResponseEntity.ok().body(order);
    }
    
    @PostMapping("/get-list-party-customers")
    public ResponseEntity getListPartyCustomers(Principal principal, @RequestBody GetListPartyCustomerInputModel input) {
        // TODO
        List<PartyCustomer> partyCustomers = partyCustomerService.getListPartyCustomers();
        return ResponseEntity.ok().body(new GetListPartyCustomerOutputModel(partyCustomers));
    }

    

    @PostMapping("/get-total-revenue")
    public ResponseEntity getTotalRevenue(Principal principal, @RequestBody GetTotalRevenueInputModel input) {

        List<String> keys = Collections.list(OrderAPIController.revenueOrderCache.keys());
        GetTotalRevenueItemOutputModel[] itemOutputModels = new GetTotalRevenueItemOutputModel[keys.size()];
        for (int i = 0; i < itemOutputModels.length; i++) {
            itemOutputModels[i] = new GetTotalRevenueItemOutputModel(keys.get(i),
                    OrderAPIController.revenueOrderCache.getRevenue(keys.get(i)));
        }
        return ResponseEntity.ok().body(new GetTotalRevenueOutputModel(itemOutputModels));
    }
}

package com.hust.baseweb.applications.tms.controller;

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
import com.hust.baseweb.applications.tms.service.DistanceTravelTimeService;
import com.hust.baseweb.repo.UserLoginRepo;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@CrossOrigin
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class TMSAPIController {
    public static final String module = TMSAPIController.class.getName();

    private CustomerRepo customerRepo;
    private OrderRepo orderRepo;
    private OrderRoleRepo orderRoleRepo;
    private UserLoginRepo userLoginRepo;

    @GetMapping("/get-assigned-delivery-routes")
    private ResponseEntity<?> getAssignedDeliveryRoutes(Principal principal) {
        System.out.println(module + "::getAssignedDeliveryRoutes, user = " + principal.getName());
//        UserLogin userLogin = userLoginRepo.findByUserLoginId(principal.getName());
        List<PartyCustomer> customers = customerRepo.findAll();
        List<OrderHeader> orders = orderRepo.findAll();
        List<OrderRole> orderRoles = orderRoleRepo.findAll();
        //List<OrderHeader> sel_orders = new ArrayList<OrderHeader>();
        //for(int i = 0; i < 3; i++)
        //	sel_orders.add(orders.get(i));
        System.out.println(module + "::getAssignedDeliveryRoutes, orders.sz = " + orders.size() + ", orderRoles.sz = " + orderRoles.size() + ", customers = " + customers.size());
        HashMap<String, OrderHeader> mapIdToOrder = new HashMap<>();
        for (OrderHeader orderHeader : orders) {
            mapIdToOrder.put(orderHeader.getOrderId(), orderHeader);
        }
        List<DeliveryCustomerModel> deliveryCustomers = new ArrayList<>();
        for (PartyCustomer partyCustomer : customers) {
            if (partyCustomer.getPostalAddress() == null || partyCustomer.getPostalAddress().size() == 0) {
                continue;
            }
            //if(!c.getPartyId().equals(userLogin.getParty().getPartyId())) continue;
            List<OrderHeader> orderOfCustomer = new ArrayList<>();
            for (OrderRole orderRole : orderRoles) {
                if (orderRole.getPartyId().toString().equals(partyCustomer.getPartyId().toString())) {
                    String orderId = orderRole.getOrderId();
                    //for(OrderHeader o: orders) if(o.getOrderId().equals(orderId))
                    //	orderOfCustomer.add(o);
                    orderOfCustomer.add(mapIdToOrder.get(orderId));
                    if (orderOfCustomer.size() > 1) {
                        break;// FOR TESTING
                    }
                }
            }
            System.out.println(module + "::::getAssignedDeliveryRoutes, find " + orderOfCustomer.size() + " orders for customers " + partyCustomer.getCustomerName());

            List<DeliveryItemModel> deliveryItemModels = new ArrayList<>();
            for (OrderHeader orderHeader : orderOfCustomer) {
                for (OrderItem orderItem : orderHeader.getOrderItems()) {
                    deliveryItemModels.add(new DeliveryItemModel(orderItem.getOrderId(), orderItem.getOrderItemSeqId(), orderItem.getProduct().getProductId(), orderItem.getQuantity()));
                }
            }
            deliveryCustomers.add(new DeliveryCustomerModel(partyCustomer, deliveryItemModels));
        }

        return ResponseEntity.ok().body(new GetAssigned2ShipperDeliveryRouteOutputModel(deliveryCustomers));
    }

    private DistanceTravelTimeService distanceTravelTimeService;

    @GetMapping("/calc-distance-travel-time")
    public ResponseEntity<?> calcDistanceTravelTime() {
        return ResponseEntity.ok(distanceTravelTimeService.calcAll());
    }
}

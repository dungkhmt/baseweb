package com.hust.baseweb.applications.tms.controller;

import com.hust.baseweb.applications.customer.repo.CustomerRepo;
import com.hust.baseweb.applications.geo.service.DistanceTravelTimePostalAddressService;
import com.hust.baseweb.applications.order.repo.OrderHeaderRepo;
import com.hust.baseweb.applications.order.repo.OrderRoleRepo;
import com.hust.baseweb.applications.tms.entity.DeliveryTripDetail;
import com.hust.baseweb.applications.tms.model.VehicleModel;
import com.hust.baseweb.applications.tms.model.deliverytrip.CompleteDeliveryShipmentItemInputModel;
import com.hust.baseweb.applications.tms.model.deliverytrip.CompleteDeliveryShipmentItemsInputModel;
import com.hust.baseweb.applications.tms.model.deliverytrip.GetDeliveryTripAssignedToDriverInputModel;
import com.hust.baseweb.applications.tms.model.deliverytrip.GetDeliveryTripAssignedToDriverOutputModel;
import com.hust.baseweb.applications.tms.service.DeliveryTripDetailService;
import com.hust.baseweb.applications.tms.service.DeliveryTripService;
import com.hust.baseweb.applications.tms.service.SolverService;
import com.hust.baseweb.applications.tms.service.statistic.StatisticDeliveryTripService;
import com.hust.baseweb.repo.UserLoginRepo;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

@RestController
@CrossOrigin
@Log4j2
public class TMSAPIController {
    public static final String module = TMSAPIController.class.getName();

    private CustomerRepo customerRepo;
    private OrderHeaderRepo orderHeaderRepo;
    private OrderRoleRepo orderRoleRepo;
    private UserLoginRepo userLoginRepo;
    private DistanceTravelTimePostalAddressService distanceTravelTimePostalAddressService;
    private DeliveryTripService deliveryTripService;
    private DeliveryTripDetailService deliveryTripDetailService;
    private StatisticDeliveryTripService statisticDeliveryTripService;

    private SolverService solverService;

    @PostMapping("/statistic-vehicle-distance")
    public ResponseEntity<?> statisticVehicleDistance(Principal prinicpal,
                                                      @RequestBody VehicleModel.InputDistanceStatistic input) {
        List<VehicleModel.Distance> distances = statisticDeliveryTripService.collectVehicleDistance(input.getFromDate(),
                input.getThruDate());
        return ResponseEntity.ok().body(distances);
    }

    @PostMapping("/get-assigned-delivery-routes")
    public ResponseEntity<?> getDeliveryTripAssignedToDriver(Principal principal,
                                                             @RequestBody GetDeliveryTripAssignedToDriverInputModel input) {
        GetDeliveryTripAssignedToDriverOutputModel deliveryTrip = deliveryTripService.getDeliveryTripAssignedToDriver(
                input.getDriverUserLoginId());

        return ResponseEntity.ok().body(deliveryTrip);
    }

    @PostMapping("/complete-shipment-items")
    public ResponseEntity<?> completeShipmentItems(Principal prinicpal,
                                                   @RequestBody CompleteDeliveryShipmentItemsInputModel input) {
        if (input.getItems() == null || input.getItems().length == 0) {
            return ResponseEntity.ok().body("OK");
        }
        log.info("completeShipmentItems, input.getItems().length = " + input.getItems().length);
        for (CompleteDeliveryShipmentItemInputModel I : input.getItems()) {
            log.info("completeShipmentItems, deliveryTripDetailId = " + I.getDeliveryTripDetailId());
            //DeliveryTripDetail dtd = deliveryTripDetailService.updateStatusDeliveryTripDetail(I.getDeliveryTripDetailId(), TMSConstants.SHIPMENT_ITEM_DELIVERED);
            DeliveryTripDetail dtd = deliveryTripDetailService.updateStatusDeliveryTripDetail(I.getDeliveryTripDetailId(),
                    "SHIPMENT_ITEM_DELIVERED");
            log.info("completeShipmentItems, deliveryTripDetailId = " + I.getDeliveryTripDetailId() + " FINISHED");
        }
        return ResponseEntity.ok().body("OK");
    }

    @Autowired
    public TMSAPIController(CustomerRepo customerRepo,
                            OrderHeaderRepo orderHeaderRepo,
                            OrderRoleRepo orderRoleRepo,
                            UserLoginRepo userLoginRepo,
                            DeliveryTripService deliveryTripService,
                            DeliveryTripDetailService deliveryTripDetailService,
                            StatisticDeliveryTripService statisticDeliveryTripService,
                            DistanceTravelTimePostalAddressService distanceTravelTimePostalAddressService,
                            SolverService solverService) {
        this.customerRepo = customerRepo;
        this.orderHeaderRepo = orderHeaderRepo;
        this.orderRoleRepo = orderRoleRepo;
        this.userLoginRepo = userLoginRepo;
        this.deliveryTripService = deliveryTripService;
        this.deliveryTripDetailService = deliveryTripDetailService;
        this.statisticDeliveryTripService = statisticDeliveryTripService;
        this.distanceTravelTimePostalAddressService = distanceTravelTimePostalAddressService;
        this.solverService = solverService;
    }

    /*
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
	*/

    @GetMapping("/calc-distance-travel-time")
    public ResponseEntity<?> calcDistanceTravelTime() {
        log.info("::calcDistanceTravelTime()");
        return ResponseEntity.ok(distanceTravelTimePostalAddressService.computeMissingDistance("HAVERSINE",
                -1,
                -1,
                -1));
    }

    @PostMapping("/solve")
    public ResponseEntity<?> solve(@RequestBody SolverService.SolverOption solverOption) throws IOException {
        log.info("::solve()");

        return ResponseEntity.ok(solverService.solve(solverOption));
    }
}

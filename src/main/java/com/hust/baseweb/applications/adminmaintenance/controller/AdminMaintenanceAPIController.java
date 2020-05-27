package com.hust.baseweb.applications.adminmaintenance.controller;

import com.hust.baseweb.applications.adminmaintenance.model.deliveryplan.DeleteAllDeliveryPlanInputModel;
import com.hust.baseweb.applications.adminmaintenance.model.salesroutes.DeleteSalesRoutesDetailInputModel;
import com.hust.baseweb.applications.adminmaintenance.service.salesroutes.SalesRouteDetailMaintenanceService;
import com.hust.baseweb.applications.adminmaintenance.service.tms.DeliveryPlanMaintenanceService;
import com.hust.baseweb.applications.logistics.service.AdminMaintenanceService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class AdminMaintenanceAPIController {

    private DeliveryPlanMaintenanceService deliveryPlanMaintenanceService;
    private SalesRouteDetailMaintenanceService salesRouteDetailMaintenanceService;

    private AdminMaintenanceService adminMaintenanceService;

    @PostMapping("/delete-all-delivery-plan")
    public ResponseEntity<?> deleteAllDeliveryPlan(
        Principal principal,
        @RequestBody DeleteAllDeliveryPlanInputModel input) {

        long cnt = deliveryPlanMaintenanceService.deleteAllDeliveryPlan();
        return ResponseEntity.ok().body(cnt);
    }

    @PostMapping("/delete-sales-routes-detail-by-party-salesman")
    public ResponseEntity<?> deleteSalesRoutesDetailByPartySalesman(
        Principal principal,
        @RequestBody DeleteSalesRoutesDetailInputModel input) {

        long cnt = salesRouteDetailMaintenanceService.deleteByPartySalesmanId(input.getPartySalesmanId());
        return ResponseEntity.ok().body(cnt);
    }

    @GetMapping("/delete-order-shipment-invoice-delivery-trip-payment")
    public ResponseEntity<?> deleteOrderShipmentInvoiceDeliveryTripPayment() {

        return ResponseEntity.ok(adminMaintenanceService.deleteAllOrders());
    }
}

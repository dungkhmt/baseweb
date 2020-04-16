package com.hust.baseweb.applications.adminmaintenance.controller;

import com.hust.baseweb.applications.adminmaintenance.model.deliveryplan.DeleteAllDeliveryPlanInputModel;
import com.hust.baseweb.applications.adminmaintenance.service.tms.DeliveryPlanMaintenanceService;
import lombok.AllArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class AdminMaintenanceAPIController {
    private DeliveryPlanMaintenanceService deliveryPlanMaintenanceService;

    @PostMapping("/delete-all-delivery-plan")
    public ResponseEntity<?> deleteAllDeliveryPlan(Principal  principal, @RequestBody DeleteAllDeliveryPlanInputModel input){
        long cnt = deliveryPlanMaintenanceService.deleteAllDeliveryPlan();
        return ResponseEntity.ok().body(cnt);
    }
}

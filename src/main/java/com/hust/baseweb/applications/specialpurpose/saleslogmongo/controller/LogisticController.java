package com.hust.baseweb.applications.specialpurpose.saleslogmongo.controller;

import com.hust.baseweb.applications.specialpurpose.saleslogmongo.model.CreatePurchaseOrderInputModel;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@CrossOrigin
@AllArgsConstructor(onConstructor = @__(@Autowired))
@Log4j2
public class LogisticController {

    @PostMapping("/mongo/create-purchase-order")
    public ResponseEntity<?> createPurchaseOrder(Principal principal, @RequestBody CreatePurchaseOrderInputModel input){
        // TODO
        return null;
    }

    @GetMapping("/mongo/get-inventory-item/{facilityId}")
    public ResponseEntity<?> getInventoryItems(Principal principal){
        // TODO: return an object of type GetInventoryItemOutputModel
        
        return null;
    }
}

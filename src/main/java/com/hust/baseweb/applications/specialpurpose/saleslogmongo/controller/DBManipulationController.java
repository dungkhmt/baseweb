package com.hust.baseweb.applications.specialpurpose.saleslogmongo.controller;

import com.hust.baseweb.applications.specialpurpose.saleslogmongo.service.LogisticService;
import com.hust.baseweb.applications.specialpurpose.saleslogmongo.service.SalesService;
import com.hust.baseweb.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@CrossOrigin
@AllArgsConstructor(onConstructor = @__(@Autowired))
@Log4j2
public class DBManipulationController {

    UserService userService;
    private final SalesService salesService;
    private final LogisticService logisticsService;

    @GetMapping("/mongo/delete-all-sales-logistics")
    public ResponseEntity<?> deleteAllSalesLogisticsData(Principal principal) {
        salesService.deleteAllRunningData();
        logisticsService.removeAllRunningData();
        return ResponseEntity.ok().body("remove all running data OK");
    }

    @GetMapping("/mongo/delete-all-products")
    public ResponseEntity<?> deleteAllProducts(Principal principal) {
        logisticsService.removeAllProductData();
        return ResponseEntity.ok().body("remove all products data OK");
    }

    @GetMapping("/mongo/delete-all-facilities")
    public ResponseEntity<?> deleteAllFacilities(Principal principal) {
        logisticsService.removeAllFacilityData();
        return ResponseEntity.ok().body("remove all facility data OK");
    }

    @GetMapping("/mongo/delete-all-customers")
    public ResponseEntity<?> deleteAllCustomers(Principal principal) {
        salesService.removeAllCustomerData();
        return ResponseEntity.ok().body("remove all customers data OK");
    }

}

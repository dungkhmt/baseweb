package com.hust.baseweb.applications.specialpurpose.saleslogmongo.controller;

import com.hust.baseweb.applications.specialpurpose.saleslogmongo.document.Facility;
import com.hust.baseweb.applications.specialpurpose.saleslogmongo.document.Product;
import com.hust.baseweb.applications.specialpurpose.saleslogmongo.model.*;
import com.hust.baseweb.applications.specialpurpose.saleslogmongo.service.LogisticService;
import com.hust.baseweb.entity.UserLogin;
import com.hust.baseweb.service.UserService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@CrossOrigin
@AllArgsConstructor(onConstructor = @__(@Autowired))
@Log4j2
public class LogisticController {

    private UserService userService;
    private final LogisticService logisticService;


    @PostMapping("mongo/create-product")
    public ResponseEntity<?> createProduct(Principal principal, @RequestBody CreateProductInputModel input) {
        Product product = logisticService.createProduct(input.getProductId(), input.getProductName(), input.getUomId());
        return ResponseEntity.ok().body(product);
    }

    @GetMapping("/mongo/get-list-products")
    public ResponseEntity<?> getListProducts(Principal principal) {
        return ResponseEntity.ok().body(logisticService.findAllProducts());
    }

    @PostMapping("/mongo/create-purchase-order")
    @ApiOperation(value = "Tạo đơn mua")
    public ResponseEntity<?> createPurchaseOrder(
        Principal principal,
        @ApiParam(value = "Dữ liệu mô tả thông tin đơn tạo")
        @RequestBody CreatePurchaseOrderInputModel input
    ) {
        return ResponseEntity.ok(logisticService.createPurchaseOrder(input));
    }

    @GetMapping("/mongo/get-inventory-item/{facilityId}")
    @ApiOperation(value = "Danh sách tồn kho")
    public ResponseEntity<GetInventoryItemOutputModel> getInventoryItems(
        Principal principal,
        @ApiParam(value = "Kho cần xem thông tin") @PathVariable String facilityId
    ) {
        return ResponseEntity.ok(logisticService.getInventoryItems(facilityId));

    }

    @PostMapping("/mongo/create-facility-of-user-login-salesman")
    @ApiOperation(value = "tao moi kho ma salesman hien tai duoc ban hang tu kho do")
    public ResponseEntity<?> createFacilityOfUserLogin(
        Principal principal,
        @RequestBody CreateFacilityInputModel input
    ) {
        // TOTO update to Facility, Organization, SalesmanFacility
        UserLogin u = userService.findById(principal.getName());
        Facility facility = logisticService.createFacilityOfSalesman(
            u.getUserLoginId(),
            input.getFacilityName(),
            input.getAddress());

        return ResponseEntity.ok().body(facility);
    }

    @GetMapping("/mongo/get-facility-of-user-login")
    @ApiOperation(value = "tra ve DS cac facility ma userlogin salesman hien tai cos quyen ban hang tu kho do")
    public ResponseEntity<?> getListFacilityOfUserLogin(Principal principal) {
        UserLogin u = userService.findById(principal.getName());
        List<FacilityModel> facilityList = logisticService.getFacilityOfSalesman(u.getUserLoginId());
        return ResponseEntity.ok().body(facilityList);
    }

    @GetMapping("mongo/get-all-facility")
    public ResponseEntity<?> getAllFacilities(Principal principal) {
        return ResponseEntity.ok().body(logisticService.getAllFacilities());
    }


}

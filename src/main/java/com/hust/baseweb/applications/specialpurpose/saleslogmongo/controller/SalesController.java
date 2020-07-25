package com.hust.baseweb.applications.specialpurpose.saleslogmongo.controller;

import com.hust.baseweb.applications.specialpurpose.saleslogmongo.model.CreateCustomerInputModel;
import com.hust.baseweb.applications.specialpurpose.saleslogmongo.model.CreateSalesOrderInputModel;
import com.hust.baseweb.applications.specialpurpose.saleslogmongo.service.SalesService;
import com.hust.baseweb.entity.UserLogin;
import com.hust.baseweb.service.UserService;
import io.swagger.annotations.ApiOperation;
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
public class SalesController {
    UserService userService;

    private final SalesService salesService;

    @PostMapping("/mongo/create-sales-order")
    @ApiOperation(value = "Tạo đơn bán")
    public ResponseEntity<?> createSalesOrder(Principal principal, @RequestBody CreateSalesOrderInputModel input) {
        //UserLogin u = userService.findById(principal.getName());

        return ResponseEntity.ok(salesService.createSalesOrder(input));
    }

    @PostMapping("/mongo/create-customer")
    @ApiOperation(value = "tao moi khach hang")
    public ResponseEntity<?> createCustomer(Principal principal, @RequestBody CreateCustomerInputModel input){
        // TOTO: create a customer of current user_login (salesman), this salesman manage the newly created customer
        //      update to the SalesmanCustomer, Customer, Organization collections
        return null;
    }
    @GetMapping("/mongo/get-customer-of-user-login")
    @ApiOperation(value = "lay danh sach khach hang quan ly bo user login hien tai")
    public ResponseEntity<?> getCustomerOfUserLogin(Principal principal){
        // TODO: get list of customers managed by current user login, use SalesmanCustomer, Organization  collections
        return null;
    }
}

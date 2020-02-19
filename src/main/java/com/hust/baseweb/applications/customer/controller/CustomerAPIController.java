package com.hust.baseweb.applications.customer.controller;

import com.hust.baseweb.applications.customer.entity.PartyCustomer;
import com.hust.baseweb.applications.customer.model.CreateCustomerInputModel;
import com.hust.baseweb.applications.customer.repo.CustomerRepo;
import com.hust.baseweb.applications.customer.service.CustomerService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;


@RestController
@CrossOrigin
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class CustomerAPIController {
    public static final String module = CustomerAPIController.class.getName();

    private CustomerRepo customerRepo;
    private CustomerService customerService;

    @GetMapping("/customers")
    public ResponseEntity<?> getCustomers(Pageable page) {
        System.out.println(module + "::getCustomers");
        Page<PartyCustomer> customers = customerRepo.findAll(page);
        return ResponseEntity.ok().body(customers);
    }

    @PostMapping("/create-customer")
    public ResponseEntity<?> createCustomer(Principal principal, @RequestBody CreateCustomerInputModel input) {
        PartyCustomer customer = customerService.save(input);
        return ResponseEntity.ok().body(customer);
    }
}

package com.hust.baseweb.applications.customer.controller;

import com.hust.baseweb.applications.customer.entity.PartyCustomer;
import com.hust.baseweb.applications.customer.entity.PartyDistributor;
import com.hust.baseweb.applications.customer.model.CreateCustomerInputModel;
import com.hust.baseweb.applications.customer.model.CreateDistributorInputModel;
import com.hust.baseweb.applications.customer.model.GetDistributorsOfUserLoginInputModel;
import com.hust.baseweb.applications.customer.repo.CustomerRepo;
import com.hust.baseweb.applications.customer.repo.DistributorRepo;
import com.hust.baseweb.applications.customer.service.CustomerService;
import com.hust.baseweb.applications.customer.service.DistributorService;
import com.hust.baseweb.entity.UserLogin;
import com.hust.baseweb.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;


@RestController
@CrossOrigin
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class CustomerAPIController {
    public static final String module = CustomerAPIController.class.getName();

    private CustomerRepo customerRepo;
    private CustomerService customerService;
    private DistributorRepo distributorRepo;
    private DistributorService distributorService;
    private UserService userService;

    @GetMapping("/customers")
    public ResponseEntity<?> getCustomers(Pageable page) {
        System.out.println(module + "::getCustomers");
        Page<PartyCustomer> customers = customerRepo.findAll(page);
        return ResponseEntity.ok().body(customers);
    }

    @GetMapping("/distributors")
    public ResponseEntity<?> getDIstributors(Pageable page) {
        System.out.println(module + "::getDistributors");
        Page<PartyDistributor> distributors = distributorRepo.findAll(page);
        return ResponseEntity.ok().body(distributors);
    }

    @PostMapping("/get-distributors-of-userlogin")
    public ResponseEntity<?> getDistributorsOfUserLogin(Principal principal, @RequestBody GetDistributorsOfUserLoginInputModel input) {
        UserLogin userLogin = userService.findById(principal.getName());
        System.out.println(module + "::getDistributorsOfUserLogin");
        // TODO: to be upgrade and revise
        List<PartyDistributor> distributors = distributorService.findDistributors();
        return ResponseEntity.ok().body(distributors);
    }

    @PostMapping("/create-customer")
    public ResponseEntity<?> createCustomer(Principal principal, @RequestBody CreateCustomerInputModel input) {
        PartyCustomer customer = customerService.save(input);
        return ResponseEntity.ok().body(customer);
    }

    @PostMapping("/create-distributor")
    public ResponseEntity<?> createDistributor(Principal principal, @RequestBody CreateDistributorInputModel input) {
        PartyDistributor distributor = distributorService.save(input);
        return ResponseEntity.ok().body(distributor);
    }
}

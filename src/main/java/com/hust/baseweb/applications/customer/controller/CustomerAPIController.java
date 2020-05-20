package com.hust.baseweb.applications.customer.controller;

import com.hust.baseweb.applications.customer.entity.PartyCustomer;
import com.hust.baseweb.applications.customer.entity.PartyDistributor;
import com.hust.baseweb.applications.customer.entity.PartyRetailOutlet;
import com.hust.baseweb.applications.customer.model.*;
import com.hust.baseweb.applications.customer.repo.CustomerRepo;
import com.hust.baseweb.applications.customer.service.CustomerService;
import com.hust.baseweb.applications.customer.service.DistributorService;
import com.hust.baseweb.applications.customer.service.RetailOutletService;
import com.hust.baseweb.applications.logistics.model.InputModel;
import com.hust.baseweb.applications.order.repo.PartyDistributorRepo;
import com.hust.baseweb.entity.PartyRelationship;
import com.hust.baseweb.entity.RoleType;
import com.hust.baseweb.entity.UserLogin;
import com.hust.baseweb.repo.PartyRepo;
import com.hust.baseweb.repo.RoleTypeRepo;
import com.hust.baseweb.service.PartyRelationshipService;
import com.hust.baseweb.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Date;
import java.util.List;
import java.util.UUID;


@RestController
@CrossOrigin
@AllArgsConstructor(onConstructor = @__(@Autowired))
@Log4j2

public class CustomerAPIController {
    public static final String module = CustomerAPIController.class.getName();

    private CustomerRepo customerRepo;
    private CustomerService customerService;
    private PartyDistributorRepo partyDistributorRepo;
    private DistributorService distributorService;
    private UserService userService;
    private RetailOutletService retailOutletService;
    private PartyRelationshipService partyRelationshipService;
    private RoleTypeRepo roleTypeRepo;
    private PartyRepo partyRepo;

    @GetMapping("/customers")
    public ResponseEntity<?> getCustomers(Pageable page) {
//        System.out.println(module + "::getCustomers");
        Page<PartyCustomer> customers = customerRepo.findAll(page);
        for (PartyCustomer c : customers
        ) {
            if (c.getPartyType() != null) {
                c.setType(c.getPartyType().getDescription());
            }
        }
        return ResponseEntity.ok().body(customers);
    }

    @GetMapping("/distributors")
    public ResponseEntity<?> getDistributors(Pageable page) {
//        System.out.println(module + "::getDistributors");
        Page<PartyDistributor> distributors = partyDistributorRepo.findAll(page);
        return ResponseEntity.ok().body(distributors);
    }

    @GetMapping(path = "/distributor/{partyDistributorId}")
    public ResponseEntity<?> getDetailDistributor(Principal principal, @PathVariable UUID partyDistributorId) {
        log.info("getDetailDistributor, partyDistributorId = " + partyDistributorId);
        DetailDistributorModel detailDistributorModel = distributorService.getDistributorDetail(partyDistributorId);
        return ResponseEntity.ok().body(detailDistributorModel);
    }

    @GetMapping(path = "/retailoutlet/{partyRetailOutletId}")
    public ResponseEntity<?> getDetailRetailOutlet(Principal principal, @PathVariable UUID partyRetailOutletId) {
        log.info("getDetailRetailOutlet, partyRetailOutletId = " + partyRetailOutletId);
        DetailRetailOutletModel detailRetailOutletModel = retailOutletService.getRetailOutletDetail(partyRetailOutletId);
        return ResponseEntity.ok().body(detailRetailOutletModel);
    }

    @PostMapping("/get-distributors-of-user-login")
    public ResponseEntity<?> getDistributorsOfUserLogin(Principal principal,
                                                        @RequestBody GetDistributorsOfUserLoginInputModel input) {
        UserLogin userLogin = userService.findById(principal.getName());
//        System.out.println(module + "::getDistributorsOfUserLogin");
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
        UserLogin u = userService.findById(principal.getName());
        PartyDistributor distributor = distributorService.save(input);

        RoleType roleType = roleTypeRepo.findByRoleTypeId("SALESMAN_SELL_FROM_DISTRIBUTOR");
        PartyRelationship partyRelationship = new PartyRelationship();
        partyRelationship.setFromParty(u.getParty());
        partyRelationship.setToParty(partyRepo.findByPartyId(distributor.getPartyId()));
        partyRelationship.setFromDate(new Date());
        partyRelationship.setRoleType(roleType);
        partyRelationship = partyRelationshipService.save(partyRelationship);

        return ResponseEntity.ok().body(distributor);
    }

    @PostMapping("/create-retail-outlet")
    public ResponseEntity<?> createRetailOutlet(Principal principal, @RequestBody CreateRetailOutletInputModel input) {
        UserLogin u = userService.findById(principal.getName());
        log.info("createRetailOutlet, user-login = " + u.getUserLoginId() + ", retail-outlet name = " +
            input.getRetailOutletName() + ", retail-outlet code = " + input.getRetailOutletCode());

        PartyRetailOutlet retailOutlet = retailOutletService.save(input);

        RoleType roleType = roleTypeRepo.findByRoleTypeId("SALESMAN_SELL_TO_RETAIL_OUTLET");
        PartyRelationship partyRelationship = new PartyRelationship();
        partyRelationship.setFromParty(u.getParty());
        partyRelationship.setToParty(partyRepo.findByPartyId(retailOutlet.getPartyId()));
        partyRelationship.setFromDate(new Date());
        partyRelationship.setRoleType(roleType);
        partyRelationship = partyRelationshipService.save(partyRelationship);

        return ResponseEntity.ok().body(retailOutlet);

    }

    @PostMapping("/get-list-customer")
    public ResponseEntity<?> getListCustomer(Principal principal, @RequestBody InputModel input) {
        log.info("getListCustomer");
        List<PartyCustomer> partyCustomerList = customerService.findAll();
        return ResponseEntity.ok().body(new GetListCustomerOutputModel(partyCustomerList));
    }

    @PostMapping("/get-list-retail-outlet")
    public ResponseEntity<?> getListRetailOutlet(Principal principal, @RequestBody InputModel input) {
        log.info("getListCustomer");
        List<PartyRetailOutlet> partyRetailOutletList = retailOutletService.findAll();
        return ResponseEntity.ok().body(new GetListRetailOutletOutputModel(partyRetailOutletList));
    }

    @PostMapping("/get-list-distributor")
    public ResponseEntity<?> getListDistributor(Principal principal, @RequestBody InputModel input) {
        log.info("getListDistributor");
        List<PartyDistributor> partyDistributorList = partyDistributorRepo.findAll();
        return ResponseEntity.ok().body(new GetListDistributorOutPutModel(partyDistributorList));
    }

    @GetMapping("/get-retail-outlet-candidates/{Id}")
    public ResponseEntity<?> getRetailOutletCandidates(Principal principal, @PathVariable("Id") String Id) {
        log.info("getRetailOutletCandidates");
        List<PartyRetailOutlet> partyRetailOutletList = retailOutletService.getRetailOutletCandidates(UUID.fromString(Id));
        return ResponseEntity.ok().body(partyRetailOutletList);
    }

    @GetMapping("/get-distributor-candidates/{Id}")
    public ResponseEntity<?> getDistributorCandidates(Principal principal, @PathVariable("Id") String Id) {
        log.info("getDistributorCandidates");
        List<PartyDistributor> partyRetailOutletList = distributorService.getDistributorCandidates(UUID.fromString(Id));
        return ResponseEntity.ok().body(partyRetailOutletList);
    }
}

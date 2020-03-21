package com.hust.baseweb.applications.sales.controller;

import com.hust.baseweb.applications.customer.entity.PartyCustomer;
import com.hust.baseweb.applications.order.model.GetListSalesmanInputModel;
import com.hust.baseweb.applications.sales.entity.CustomerSalesman;
import com.hust.baseweb.applications.sales.model.customersalesman.AssignCustomer2SalesmanInputModel;
import com.hust.baseweb.applications.sales.model.customersalesman.GetCustomersOfSalesmanInputModel;
import com.hust.baseweb.applications.sales.model.customersalesman.GetSalesmanOutputModel;
import com.hust.baseweb.applications.sales.service.CustomerSalesmanService;
import com.hust.baseweb.applications.sales.service.PartySalesmanService;
import com.hust.baseweb.entity.UserLogin;
import com.hust.baseweb.service.UserService;
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

public class SalesAPIController {
    private UserService userService;

    private CustomerSalesmanService customerSalesmanService;
    private PartySalesmanService partySalesmanService;

    @PostMapping("/get-list-salesmans")
    public ResponseEntity getListSalesmans(Principal principal, @RequestBody GetListSalesmanInputModel input) {
        // TODO
        List<GetSalesmanOutputModel> salesman = partySalesmanService.findAllSalesman();
        return ResponseEntity.ok().body(salesman);
    }

    @PostMapping("/assign-customer-2-salesman")
    public ResponseEntity<?> assignCustomer2Salesman(Principal principal, @RequestBody AssignCustomer2SalesmanInputModel input) {
        log.info("assignCustomer2Salesman, partyCustomerId = " + input.getPartyCustomerId() + ", partySalesmanId = " + input.getPartySalesmanId());
        CustomerSalesman cs = customerSalesmanService.save(input.getPartyCustomerId(), input.getPartySalesmanId());

        return ResponseEntity.ok().body(cs);
    }

    @PostMapping("/get-customers-of-salesman")
    public ResponseEntity<?> getCustomersOfSalesman(Principal principal, @RequestBody GetCustomersOfSalesmanInputModel input) {
        UserLogin userLogin = userService.findById(principal.getName());

        log.info("getCustomersOfSalesman, userlogin = " + userLogin.getUserLoginId());

        //List<PartyCustomer> lst = customerSalesmanService.getCustomersOfSalesman(userLogin.getParty().getPartyId());
        List<PartyCustomer> lst = customerSalesmanService.getCustomersOfSalesman(input.getPartySalesman());
        return ResponseEntity.ok().body(lst);
    }

    @GetMapping("get-customers-of-userlogin")
    public ResponseEntity<?> getCustomersOfUserLogin(Principal principal) {
        UserLogin userLogin = userService.findById(principal.getName());

        log.info("getCustomersOfSalesman, userlogin = " + userLogin.getUserLoginId());

        List<PartyCustomer> lst = customerSalesmanService.getCustomersOfSalesman(userLogin.getParty().getPartyId());

        return ResponseEntity.ok().body(lst);
    }
    
}	

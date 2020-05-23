package com.hust.baseweb.applications.sales.controller;

import com.hust.baseweb.applications.customer.entity.PartyCustomer;
import com.hust.baseweb.applications.customer.entity.PartyDistributor;
import com.hust.baseweb.applications.customer.entity.PartyRetailOutlet;
import com.hust.baseweb.applications.customer.model.CustomerDistributorSalesmanInputModel;
import com.hust.baseweb.applications.customer.model.GetListRetailOutletOutputModel;
import com.hust.baseweb.applications.customer.repo.RetailOutletPagingRepo;
import com.hust.baseweb.applications.customer.service.DistributorService;
import com.hust.baseweb.applications.customer.service.RetailOutletService;
import com.hust.baseweb.applications.geo.entity.PostalAddress;
import com.hust.baseweb.applications.order.model.GetListSalesmanInputModel;
import com.hust.baseweb.applications.order.repo.PartyCustomerRepo;
import com.hust.baseweb.applications.order.repo.PartyDistributorRepo;
import com.hust.baseweb.applications.sales.entity.CustomerSalesman;
import com.hust.baseweb.applications.sales.entity.CustomerSalesmanVendor;
import com.hust.baseweb.applications.sales.entity.PartySalesman;
import com.hust.baseweb.applications.sales.entity.RetailOutletSalesmanVendor;
import com.hust.baseweb.applications.sales.model.ListSalesmanOutputModel;
import com.hust.baseweb.applications.sales.model.customersalesman.AssignCustomer2SalesmanInputModel;
import com.hust.baseweb.applications.sales.model.customersalesman.GetCustomersOfSalesmanInputModel;
import com.hust.baseweb.applications.sales.model.customersalesman.GetSalesmanOutputModel;
import com.hust.baseweb.applications.sales.model.distributor.ListDistributorOutputModel;
import com.hust.baseweb.applications.sales.model.retailoutletsalesmandistributor.GetListRetailOutletsOfSalesmanAndDistributorInputModel;
import com.hust.baseweb.applications.sales.model.retailoutletsalesmandistributor.RetailOutletSalesmanDistributorInputModel;
import com.hust.baseweb.applications.sales.model.salesman.SalesmanOutputModel;
import com.hust.baseweb.applications.sales.model.salesmandistributor.AddSalesmanDistributorInputModel;
import com.hust.baseweb.applications.sales.model.salesmandistributor.GetListDistributorsOfSalesmanInputModel;
import com.hust.baseweb.applications.sales.model.salesmanretailoutlet.AddSalesmanRetailOutletInputModel;
import com.hust.baseweb.applications.sales.repo.*;
import com.hust.baseweb.applications.sales.service.CustomerSalesmanService;
import com.hust.baseweb.applications.sales.service.PartySalesmanService;
import com.hust.baseweb.applications.sales.service.RetailOutletSalesmanVendorService;
import com.hust.baseweb.entity.*;
import com.hust.baseweb.model.PersonModel;
import com.hust.baseweb.repo.RoleTypeRepo;
import com.hust.baseweb.service.PartyRelationshipService;
import com.hust.baseweb.service.PartyService;
import com.hust.baseweb.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

//import javax.xml.ws.Response;


@RestController
@CrossOrigin
@AllArgsConstructor(onConstructor = @__(@Autowired))
@Log4j2

public class SalesAPIController {
    private UserService userService;
    private PartyCustomerRepo partyCustomerRepo;
    private CustomerSalesmanService customerSalesmanService;
    private PartySalesmanService partySalesmanService;
    private PartySalesmanPagingRepo partySalesmanPagingRepo;
    private CustomerSalesmanVendorPagingRepo customerSalesmanVendorPagingRepo;
    private PartyDistributorRepo partyDistributorRepo;
    private CustomerSalesmanVendorRepo customerSalesmanVendorRepo;
    private RetailOutletSalesmanVendorPagingRepo retailOutletSalesmanVendorPagingRepo;
    private RetailOutletSalesmanVendorRepo retailOutletSalesmanVendorRepo;
    private RetailOutletSalesmanVendorService retailOutletSalesmanVendorService;
    private RetailOutletService retailOutletService;
    private RetailOutletPagingRepo retailOutletRepo;
    private PartyRelationshipService partyRelationshipService;
    private PartyService partyService;
    private RoleTypeRepo roleTypeRepo;
    private DistributorService distributorService;

    @PostMapping("/get-list-salesmans")
    public ResponseEntity getListSalesmen(Principal principal, @RequestBody GetListSalesmanInputModel input) {
        // TODO
        List<GetSalesmanOutputModel> salesman = partySalesmanService.findAllSalesman();
        return ResponseEntity.ok().body(salesman);
    }

    @PostMapping("/get-list-all-salesmans")
    public ResponseEntity getListAllSalesmen(Principal principal, @RequestBody GetListSalesmanInputModel input) {
        // TODO
        List<PartySalesman> salesman = partySalesmanService.findAll();
        List<SalesmanOutputModel> salesmanModel = new ArrayList<>();
        for (PartySalesman sm : salesman) {
            UserLogin u = userService.findUserLoginByPartyId(sm.getPartyId());
            SalesmanOutputModel so = new SalesmanOutputModel();
            so.setPartyId(sm.getPartyId());
            so.setUserLoginId(u.getUserLoginId());
            so.setFullName(sm.getPerson().getLastName() +
                " " +
                sm.getPerson().getMiddleName() +
                " " +
                sm.getPerson().getFirstName());
            salesmanModel.add(so);
        }
        ListSalesmanOutputModel res = new ListSalesmanOutputModel(salesmanModel);
        return ResponseEntity.ok().body(res);
    }


    @PostMapping("/assign-customer-2-salesman")
    public ResponseEntity<?> assignCustomer2Salesman(Principal principal,
                                                     @RequestBody AssignCustomer2SalesmanInputModel input) {
        log.info("assignCustomer2Salesman, partyCustomerId = " +
            input.getPartyCustomerId() +
            ", partySalesmanId = " +
            input.getPartySalesmanId());
        CustomerSalesman cs = customerSalesmanService.save(input.getPartyCustomerId(), input.getPartySalesmanId());

        return ResponseEntity.ok().body(cs);
    }

    @PostMapping("/get-customers-of-salesman")
    public ResponseEntity<?> getCustomersOfSalesman(Principal principal,
                                                    @RequestBody GetCustomersOfSalesmanInputModel input) {
        UserLogin userLogin = userService.findById(principal.getName());

        log.info("getCustomersOfSalesman, user-login = " + userLogin.getUserLoginId());

        //List<PartyCustomer> lst = customerSalesmanService.getCustomersOfSalesman(userLogin.getParty().getPartyId());
        List<PartyCustomer> lst = customerSalesmanService.getCustomersOfSalesman(input.getPartySalesman());
        return ResponseEntity.ok().body(lst);
    }

    @GetMapping("/get-customers-of-user-login")
    public ResponseEntity<?> getCustomersOfUserLogin(Principal principal) {
        UserLogin userLogin = userService.findById(principal.getName());

        log.info("getCustomersOfUserLogin, user-login = " + userLogin.getUserLoginId());

        List<PartyCustomer> lst = customerSalesmanService.getCustomersOfSalesman(userLogin.getParty().getPartyId());

        return ResponseEntity.ok().body(lst);
    }

    @GetMapping("/get-retail-outlets-of-user-login-salesman")
    public ResponseEntity<?> getRetailOutletsOfUserLoginSalesman(Principal principal) {
        UserLogin userLogin = userService.findById(principal.getName());

        log.info("getRetailOutletsOfUserLoginSalesman, user-login = " + userLogin.getUserLoginId());
        RoleType roleType = roleTypeRepo.findByRoleTypeId("SALESMAN_SELL_TO_RETAIL_OUTLET");
        //List<PartyRetailOutlet> partyRetailOutletList = retailOutletService.findAll();
        List<PartyRelationship> partyRelationships = partyRelationshipService.findAllByFromPartyAndRoleTypeAndThruDate(
            userLogin.getParty(), roleType, null);
        //log.info("getRetailOutletsOfUserLoginSalesman, user-login = " + userLogin.getUserLoginId() + " got partyRelationships.sz = "
        //+ partyRelationships.size());
        List<UUID> partyIds = partyRelationships.stream().map(
            p -> p.getToParty().getPartyId()).collect(Collectors.toList());
        //log.info("getRetailOutletsOfUserLoginSalesman, user-login = " + userLogin.getUserLoginId() + ", partyId = "
        //+ userLogin.getParty().getPartyId() + ", partyIds.sz = " + partyIds.size());
        //for(UUID partyId: partyIds){
        //   log.info("getRetailOutletsOfUserLoginSalesman, user-login = " + userLogin.getUserLoginId() + ", partyId in = " + partyId);
        //}
        List<PartyRetailOutlet> partyRetailOutletList = retailOutletService.findByPartyIdIn(partyIds);
        return ResponseEntity.ok().body(partyRetailOutletList);
    }

    @PostMapping("/get-list-retail-outlets-of-salesman-and-distributor")
    public ResponseEntity<?> getListRetailOutletsOfSalesmanAndDistributor(Principal principal,
                                                                          @RequestBody GetListRetailOutletsOfSalesmanAndDistributorInputModel input) {
        List<PartyRetailOutlet> partyRetailOutlets = retailOutletSalesmanVendorService.getListRetailOutletOfSalesmanAndDistributor(
            input.getPartySalesmanId(),
            input.getPartyDistributorId());
        return ResponseEntity.ok().body(new GetListRetailOutletOutputModel(partyRetailOutlets));
    }

    @PostMapping("/get-retail-outlet-salesman-distributor")
    public ResponseEntity<?> getRetailOutletSalesmanDistributor(Principal principal,
                                                                @RequestBody RetailOutletSalesmanDistributorInputModel input) {
        RetailOutletSalesmanVendor retailOutletSalesmanVendor = retailOutletSalesmanVendorService.getRetailOutletSalesmanDistributor(
            input.getPartyRetailOutletId(), input.getPartySalesmanId(), input.getPartyDistributorId());
        return ResponseEntity.ok().body(retailOutletSalesmanVendor);
    }

    @GetMapping("/get-page-retail-outlets-of-user-login-salesman")
    public ResponseEntity<?> getPageRetailOutletsOfUserLoginSalesman(Principal principal, Pageable page) {
        UserLogin userLogin = userService.findById(principal.getName());

        log.info("getPageRetailOutletsOfUserLoginSalesman, user-login = " + userLogin.getUserLoginId());
        RoleType roleType = roleTypeRepo.findByRoleTypeId("SALESMAN_SELL_TO_RETAIL_OUTLET");
        //List<PartyRetailOutlet> partyRetailOutletList = retailOutletService.findAll();
        List<PartyRelationship> partyRelationships = partyRelationshipService.findAllByFromPartyAndRoleTypeAndThruDate(
            userLogin.getParty(), roleType, null);
        //log.info("getRetailOutletsOfUserLoginSalesman, user-login = " + userLogin.getUserLoginId() + " got partyRelationships.sz = "
        //+ partyRelationships.size());
        List<UUID> partyIds = partyRelationships.stream().map(
            p -> p.getToParty().getPartyId()).collect(Collectors.toList());
        //log.info("getRetailOutletsOfUserLoginSalesman, user-login = " + userLogin.getUserLoginId() + ", partyId = "
        //+ userLogin.getParty().getPartyId() + ", partyIds.sz = " + partyIds.size());
        //for(UUID partyId: partyIds){
        //   log.info("getRetailOutletsOfUserLoginSalesman, user-login = " + userLogin.getUserLoginId() + ", partyId in = " + partyId);
        //}


        Page<PartyRetailOutlet> partyRetailOutletList = retailOutletService.findByPartyIdIn(partyIds, page);
        //Page<PartyRetailOutlet> partyRetailOutletList = retailOutletService.findAll(page);

        return ResponseEntity.ok().body(partyRetailOutletList);
    }

    @GetMapping("/get-distributors-of-user-login-salesman")
    public ResponseEntity<?> getDistributorsOfUserLoginSalesman(Principal principal) {
        UserLogin userLogin = userService.findById(principal.getName());

        log.info("getDistributorsOfUserLoginSalesman, user-login = " + userLogin.getUserLoginId());
        RoleType roleType = roleTypeRepo.findByRoleTypeId("SALESMAN_SELL_FROM_DISTRIBUTOR");
        //List<PartyRetailOutlet> partyRetailOutletList = retailOutletService.findAll();
        List<PartyRelationship> partyRelationships = partyRelationshipService.findAllByFromPartyAndRoleTypeAndThruDate(
            userLogin.getParty(), roleType, null);
        //log.info("getRetailOutletsOfUserLoginSalesman, user-login = " + userLogin.getUserLoginId() + " got partyRelationships.sz = "
        //+ partyRelationships.size());
        List<UUID> partyIds = partyRelationships.stream().map(
            p -> p.getToParty().getPartyId()).collect(Collectors.toList());

        //List<PartyDistributor> partyDistributorList = distributorRepo.findAll();
        List<PartyDistributor> partyDistributorList = distributorService.findAllByPartyIdIn(partyIds);

        return ResponseEntity.ok().body(partyDistributorList);
    }

    @PostMapping("/get-distributors-of-salesman")
    public ResponseEntity<?> getDistributorsOfSalesman(Principal principal,
                                                       @RequestBody GetListDistributorsOfSalesmanInputModel input) {
        UserLogin userLogin = userService.findById(principal.getName());

        log.info("getDistributorsOfSalesman, user-login = " + userLogin.getUserLoginId());
        RoleType roleType = roleTypeRepo.findByRoleTypeId("SALESMAN_SELL_FROM_DISTRIBUTOR");
        //List<PartyRetailOutlet> partyRetailOutletList = retailOutletService.findAll();
        Party partySalesman = partyService.findByPartyId(input.getPartySalesmanId());
        List<PartyRelationship> partyRelationships = partyRelationshipService.findAllByFromPartyAndRoleTypeAndThruDate(
            partySalesman, roleType, null);
        //log.info("getRetailOutletsOfUserLoginSalesman, user-login = " + userLogin.getUserLoginId() + " got partyRelationships.sz = "
        //+ partyRelationships.size());
        List<UUID> partyIds = partyRelationships.stream().map(
            p -> p.getToParty().getPartyId()).collect(Collectors.toList());

        //List<PartyDistributor> partyDistributorList = distributorRepo.findAll();
        List<PartyDistributor> partyDistributorList = distributorService.findAllByPartyIdIn(partyIds);

        return ResponseEntity.ok().body(new ListDistributorOutputModel(partyDistributorList));
    }

    @GetMapping("/get-page-distributors-of-user-login-salesman")
    public ResponseEntity<?> getPageDistributorsOfUserLoginSalesman(Principal principal, Pageable page) {
        UserLogin userLogin = userService.findById(principal.getName());

        log.info("getDistributorsOfUserLoginSalesman, user-login = " + userLogin.getUserLoginId());
        RoleType roleType = roleTypeRepo.findByRoleTypeId("SALESMAN_SELL_FROM_DISTRIBUTOR");
        //List<PartyRetailOutlet> partyRetailOutletList = retailOutletService.findAll();
        List<PartyRelationship> partyRelationships = partyRelationshipService.findAllByFromPartyAndRoleTypeAndThruDate(
            userLogin.getParty(), roleType, null);
        //log.info("getRetailOutletsOfUserLoginSalesman, user-login = " + userLogin.getUserLoginId() + " got partyRelationships.sz = "
        //+ partyRelationships.size());
        List<UUID> partyIds = partyRelationships.stream().map(
            p -> p.getToParty().getPartyId()).collect(Collectors.toList());

        //List<PartyDistributor> partyDistributorList = distributorRepo.findAll();
        //List<PartyDistributor> partyDistributorList = distributorService.findAllByPartyIdIn(partyIds);
        Page<PartyDistributor> partyDistributorList = distributorService.findAllByPartyIdIn(partyIds, page);
        return ResponseEntity.ok().body(partyDistributorList);
    }

    private PartyRelationship addSalesmanSellFromDistributorIfNot(UUID partySalesmanId, UUID partyDistributorId) {
        RoleType roleType = roleTypeRepo.findByRoleTypeId("SALESMAN_SELL_FROM_DISTRIBUTOR");
        Party fromParty = partyService.findByPartyId(partySalesmanId);
        Party toParty = partyService.findByPartyId(partyDistributorId);
        List<PartyRelationship> partyRelationships = partyRelationshipService.findAllByFromPartyAndToPartyAndRoleTypeAndThruDate(
            fromParty,
            toParty,
            roleType,
            null);
        if (partyRelationships != null && partyRelationships.size() > 0) {
            return null;
        }
        Date now = new Date();
        PartyRelationship partyRelationship = new PartyRelationship();
        partyRelationship.setFromParty(fromParty);
        partyRelationship.setToParty(toParty);
        partyRelationship.setRoleType(roleType);
        partyRelationship.setFromDate(now);
        partyRelationship = partyRelationshipService.save(partyRelationship);
        return partyRelationship;
    }

    private PartyRelationship addSalesmanSell2RetailOutletIfNot(UUID partySalesmanId, UUID partyRetailOutletId) {
        RoleType roleType = roleTypeRepo.findByRoleTypeId("SALESMAN_SELL_TO_RETAIL_OUTLET");
        Party fromParty = partyService.findByPartyId(partySalesmanId);
        Party toParty = partyService.findByPartyId(partyRetailOutletId);
        List<PartyRelationship> partyRelationships = partyRelationshipService.findAllByFromPartyAndToPartyAndRoleTypeAndThruDate(
            fromParty,
            toParty,
            roleType,
            null);
        if (partyRelationships != null && partyRelationships.size() > 0) {
            return null;
        }
        Date now = new Date();
        PartyRelationship partyRelationship = new PartyRelationship();
        partyRelationship.setFromParty(fromParty);
        partyRelationship.setToParty(toParty);
        partyRelationship.setRoleType(roleType);
        partyRelationship.setFromDate(now);
        partyRelationship = partyRelationshipService.save(partyRelationship);
        return partyRelationship;
    }

    @PostMapping("/add-salesman-sell-from-distributor")
    public ResponseEntity<?> addSalesmanSellFromDistributor(Principal principal,
                                                            @RequestBody AddSalesmanDistributorInputModel input) {
        log.info("addSalesmanSellFromDistributor");
        PartyRelationship partyRelationship = addSalesmanSellFromDistributorIfNot(input.getSalesmanId(),
            input.getDistributorId());
        return ResponseEntity.ok().body("OK");
    }

    @PostMapping("/add-salesman-sell-to-retail-outlet")
    public ResponseEntity<?> addSalesmanSellToRetailOutlet(Principal principal,
                                                           @RequestBody AddSalesmanRetailOutletInputModel input) {
        log.info("addSalesmanSellFromDistributor");
        PartyRelationship partyRelationship = addSalesmanSell2RetailOutletIfNot(input.getSalesmanId(),
            input.getRetailOutletId());
        return ResponseEntity.ok().body("OK");
    }


    @PostMapping("/create-salesman")
    public ResponseEntity<?> createSalesman(Principal principal, @RequestBody PersonModel input) {
        log.info("createSalesman");
        PartySalesman partySalesman = partySalesmanService.save(input);
        log.info("1");
        if (partySalesman == null) {
            log.info("null");
            return ResponseEntity.status(HttpStatus.CONFLICT).body("conflict");
        } else {
            log.info("!=null");
            return ResponseEntity.ok().body(partySalesman);
        }
    }

    @GetMapping("/get-all-salesman")
    public ResponseEntity<?> getAllSalesman(Principal principal) {
        /**
         * Return all salesman in DB
         */

        log.info("getAllSalesman");
        List<GetSalesmanOutputModel> partySalesmanList = partySalesmanService.findAllSalesman();
        return ResponseEntity.ok().body(partySalesmanList);
    }

    @GetMapping("/get-list-salesman")
    public ResponseEntity<?> getListSalesman(Pageable pageable, @RequestParam(required = false) String param) {
        log.info("getListSalesman");

        Page<PartySalesman> partySalesmanPage = partySalesmanPagingRepo.findAll(pageable);
        for (PartySalesman partySalesman : partySalesmanPage) {
            Person p = partySalesman.getPerson();
            String name = "" + p.getFirstName() + " " + p.getMiddleName() + " " + p.getLastName();
            partySalesman.setName(name);
            partySalesman.setUserName(userService.findUserLoginByPartyId(partySalesman.getPartyId()).getUserLoginId());
        }

        return ResponseEntity.ok().body(partySalesmanPage);
    }

    @GetMapping("/salesman-detail/{partyId}")
    public ResponseEntity<?> getDetailSalesman(Pageable pageable,
                                               @RequestParam(required = false) String param,
                                               @PathVariable String partyId) {
        log.info("getDetailSalesman");
        PartySalesman partySalesman = partySalesmanService.findById(UUID.fromString(partyId));

        /*
        Page<CustomerSalesmanVendor> customerSalesmanVendorPage = customerSalesmanVendorPagingRepo.findByPartySalesmanAndThruDate(partySalesman,null,pageable);
        for (CustomerSalesmanVendor customerSalesmanVendor: customerSalesmanVendorPage ) {
            customerSalesmanVendor.setCustomerName(customerSalesmanVendor.getPartyCustomer().getCustomerName());
            customerSalesmanVendor.setCustomerCode(customerSalesmanVendor.getPartyCustomer().getCustomerCode());
            customerSalesmanVendor.setPartyDistributorName(customerSalesmanVendor.getPartyDistributor().getDistributorName());
            String address = "";
            List<PostalAddress> postalAddresses = customerSalesmanVendor.getPartyCustomer().getPostalAddress();
            for (PostalAddress postalAddress: postalAddresses){
                address += postalAddress.getAddress() + "; ";
            }
            customerSalesmanVendor.setAddress(address);
        }
        */
        Page<RetailOutletSalesmanVendor> retailOutletSalesmanVendorPage = retailOutletSalesmanVendorPagingRepo.findByPartySalesmanAndThruDate(
            partySalesman,
            null,
            pageable);
        for (RetailOutletSalesmanVendor retailOutletSalesmanVendor : retailOutletSalesmanVendorPage) {
            retailOutletSalesmanVendor.setRetailOutletName(retailOutletSalesmanVendor.getPartyRetailOutlet()
                .getRetailOutletName());
            retailOutletSalesmanVendor.setRetailOutletCode(retailOutletSalesmanVendor.getPartyRetailOutlet()
                .getRetailOutletCode());
            retailOutletSalesmanVendor.setPartyDistributorName(retailOutletSalesmanVendor.getPartyDistributor()
                .getDistributorName());
            String address = "";
            List<PostalAddress> postalAddresses = retailOutletSalesmanVendor.getPartyRetailOutlet().getPostalAddress();
            for (PostalAddress postalAddress : postalAddresses) {
                address += postalAddress.getAddress() + "; ";
            }
            retailOutletSalesmanVendor.setAddress(address);

        }

        return ResponseEntity.ok().body(retailOutletSalesmanVendorPage);
    }

    @PostMapping("/add-customer-distributor-salesman/{partyId}")
    public ResponseEntity<?> addCustomerDistributorSalesman(@PathVariable String partyId,
                                                            @RequestBody CustomerDistributorSalesmanInputModel input) {
        log.info("addCustomerDistributorSalesman {}", partyId);
        PartyCustomer partyCustomer = partyCustomerRepo.findByPartyId(UUID.fromString(input.getPartyCustomerId()));
        PartyDistributor partyDistributor = partyDistributorRepo.findByPartyId(UUID.fromString(input.getPartyDistributorId()));
        PartySalesman partySalesman = partySalesmanService.findById(UUID.fromString(partyId));
        List<CustomerSalesmanVendor> lst = customerSalesmanVendorRepo.findAllByPartySalesmanAndPartyCustomerAndPartyDistributorAndThruDate(
            partySalesman,
            partyCustomer,
            partyDistributor,
            null);
        if (lst != null && lst.size() > 0) {
            return ResponseEntity.status(HttpStatus.ALREADY_REPORTED).body("DUPLICATE");
        }
        CustomerSalesmanVendor customerSalesmanVendor = new CustomerSalesmanVendor();
        customerSalesmanVendor.setPartyCustomer(partyCustomer);
        customerSalesmanVendor.setPartySalesman(partySalesman);
        customerSalesmanVendor.setPartyDistributor(partyDistributor);
        customerSalesmanVendor.setFromDate(new Date());// take current date-time
        customerSalesmanVendorRepo.save(customerSalesmanVendor);
        return ResponseEntity.ok(input);
    }

    //@PostMapping("/add-retail-outlet-distributor-salesman/{partyId}")
    //public ResponseEntity<?> addRetailOutletDistributorSalesman(@PathVariable String partyId, @RequestBody RetailOutletSalesmanDistributorInputModel input){
    @PostMapping("/add-retail-outlet-distributor-salesman")
    @Transactional
    public ResponseEntity<?> addRetailOutletDistributorSalesman(Principal principal,
                                                                @RequestBody RetailOutletSalesmanDistributorInputModel input) {
        log.info("addRetailOutletDistributorSalesman salesmanId = ", input.getPartySalesmanId());
        PartyRetailOutlet partyRetailOutlet = retailOutletRepo.findByPartyId(input.getPartyRetailOutletId());

        if (partyRetailOutlet == null) {
            log.info("addRetailOutletDistributorSalesman, retail outlet " +
                input.getPartyRetailOutletId() +
                " cannot be FOUND");
        }

        PartyDistributor partyDistributor = partyDistributorRepo.findByPartyId(input.getPartyDistributorId());

        if (partyDistributor == null) {
            log.info("addRetailOutletDistributorSalesman, distributor " +
                input.getPartyDistributorId() +
                " cannot be FOUND");
        }

        PartySalesman partySalesman = partySalesmanService.findById(input.getPartySalesmanId());

        if (partySalesman == null) {
            log.info("addRetailOutletDistributorSalesman, salesman " + input.getPartySalesmanId() + " cannot be FOUND");
        }
        List<RetailOutletSalesmanVendor> lst = retailOutletSalesmanVendorRepo.findAllByPartySalesmanAndPartyRetailOutletAndPartyDistributorAndThruDate(
            partySalesman,
            partyRetailOutlet,
            partyDistributor,
            null);
        if (lst != null && lst.size() > 0) {
            log.info("addRetailOutletDistributorSalesman, lst.sz = " + lst.size() + " --> DUPLICATED ???");
            return ResponseEntity.status(HttpStatus.ALREADY_REPORTED).body("DUPLICATE");
        }

        RetailOutletSalesmanVendor retailOutletSalesmanVendor = new RetailOutletSalesmanVendor();
        retailOutletSalesmanVendor.setPartyRetailOutlet(partyRetailOutlet);
        retailOutletSalesmanVendor.setPartySalesman(partySalesman);
        retailOutletSalesmanVendor.setPartyDistributor(partyDistributor);
        retailOutletSalesmanVendor.setFromDate(new Date());// take current date-time
        retailOutletSalesmanVendorRepo.save(retailOutletSalesmanVendor);

        // add salesman-retailoutlet
        PartyRelationship partyRelationship = addSalesmanSell2RetailOutletIfNot(input.getPartySalesmanId(),
            input.getPartyRetailOutletId());

        // add salesman-distributor
        partyRelationship = addSalesmanSellFromDistributorIfNot(input.getPartySalesmanId(),
            input.getPartyDistributorId());

        return ResponseEntity.ok(input);
    }

    @GetMapping("/delete-customer-distributor-salesman/{Id}")
    public void deleteCustomerDistributorSalesman(@PathVariable String Id) {
        CustomerSalesmanVendor customerSalesmanVendor = customerSalesmanVendorRepo.findByCustomerSalesmanVendorId(UUID.fromString(
            Id));
        //customerSalesmanVendorRepo.delete(customerSalesmanVendor);// do not delete physically
        customerSalesmanVendor.setThruDate(new Date());// set thru_date by current date-time
        customerSalesmanVendorRepo.save(customerSalesmanVendor);
    }

    @GetMapping("/delete-retail-outlet-distributor-salesman/{Id}")
    public void deleteRetailOutletDistributorSalesman(@PathVariable String Id) {
        RetailOutletSalesmanVendor retailOutletSalesmanVendor = retailOutletSalesmanVendorRepo.findByRetailOutletSalesmanVendorId(
            UUID.fromString(Id));
        //customerSalesmanVendorRepo.delete(customerSalesmanVendor);// do not delete physically
        retailOutletSalesmanVendor.setThruDate(new Date());// set thru_date by current date-time
        retailOutletSalesmanVendorRepo.save(retailOutletSalesmanVendor);
    }


}

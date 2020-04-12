package com.hust.baseweb.applications.sales.controller;

import com.hust.baseweb.applications.customer.entity.PartyCustomer;
import com.hust.baseweb.applications.customer.entity.PartyDistributor;
import com.hust.baseweb.applications.customer.entity.PartyRetailOutlet;
import com.hust.baseweb.applications.customer.model.CustomerDistributorSalesmanInputModel;
import com.hust.baseweb.applications.customer.repo.DistributorRepo;
import com.hust.baseweb.applications.customer.repo.RetailOutletRepo;
import com.hust.baseweb.applications.geo.entity.PostalAddress;
import com.hust.baseweb.applications.logistics.model.ModelCreateProductInput;
import com.hust.baseweb.applications.order.model.GetListSalesmanInputModel;
import com.hust.baseweb.applications.order.repo.PartyCustomerRepo;
import com.hust.baseweb.applications.order.repo.PartyDistributorRepo;
import com.hust.baseweb.applications.sales.entity.CustomerSalesman;
import com.hust.baseweb.applications.sales.entity.CustomerSalesmanVendor;
import com.hust.baseweb.applications.sales.entity.PartySalesman;
import com.hust.baseweb.applications.sales.entity.RetailOutletSalesmanVendor;
import com.hust.baseweb.applications.sales.model.customersalesman.AssignCustomer2SalesmanInputModel;
import com.hust.baseweb.applications.sales.model.customersalesman.GetCustomersOfSalesmanInputModel;
import com.hust.baseweb.applications.sales.model.customersalesman.GetSalesmanOutputModel;
import com.hust.baseweb.applications.sales.model.retailoutletsalesmandistributor.RetailOutletSalesmanDistributorInputModel;
import com.hust.baseweb.applications.sales.repo.*;
import com.hust.baseweb.applications.sales.service.CustomerSalesmanService;
import com.hust.baseweb.applications.sales.service.PartySalesmanService;
import com.hust.baseweb.applications.tms.repo.PartyDriverRepo;
import com.hust.baseweb.entity.Person;
import com.hust.baseweb.entity.UserLogin;
import com.hust.baseweb.model.PersonModel;
import com.hust.baseweb.service.UserService;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
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

    private RetailOutletRepo retailOutletRepo;

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

        log.info("getCustomersOfUserLogin, userlogin = " + userLogin.getUserLoginId());

        List<PartyCustomer> lst = customerSalesmanService.getCustomersOfSalesman(userLogin.getParty().getPartyId());

        return ResponseEntity.ok().body(lst);
    }



    @PostMapping("/create-salesman")
    public ResponseEntity<?> createSalesman(Principal principal, @RequestBody PersonModel input){
        log.info("createSalesman");
    	PartySalesman partySalesman = partySalesmanService.save(input);
    	log.info("1");
    	if(partySalesman == null){
    	    log.info("null");
    		return ResponseEntity.status(HttpStatus.CONFLICT).body("conflict");
    	}else{
    	    log.info("!=null");
    		return ResponseEntity.ok().body(partySalesman);
    	}
    }
    @GetMapping("/get-list-salesman")
    public ResponseEntity<?> getListSaleman(Pageable pageable, @RequestParam(required = false) String param){
        log.info("getListSaleman");

        Page<PartySalesman> partySalesmanPage = partySalesmanPagingRepo.findAll(pageable);
        for(PartySalesman partySalesman: partySalesmanPage){
            Person p = partySalesman.getPerson();
            String name = "" + p.getFirstName() + " " + p.getMiddleName() + " " + p.getLastName();
            partySalesman.setName(name);
            partySalesman.setUserName(userService.findUserLoginByPartyId(partySalesman.getPartyId()).getUserLoginId());
        }

        return ResponseEntity.ok().body(partySalesmanPage);
    }

    @GetMapping("/salesman-detail/{partyId}")
    public ResponseEntity<?> getDetailSalesman(Pageable pageable,@RequestParam(required = false) String param, @PathVariable String partyId){
        log.info("getDetailSalesman");
        PartySalesman partySalesman = partySalesmanService.findById(UUID.fromString(partyId));

        /*
        Page<CustomerSalesmanVendor> customerSalesmanVendorPage = customerSalesmanVendorPagingRepo.findByPartySalesmanAndThruDate(partySalesman,null,pageable);
        for (CustomerSalesmanVendor customerSalesmanVendor: customerSalesmanVendorPage ) {
            customerSalesmanVendor.setCustomerName(customerSalesmanVendor.getPartyCustomer().getCustomerName());
            customerSalesmanVendor.setCustomerCode(customerSalesmanVendor.getPartyCustomer().getCustomerCode());
            customerSalesmanVendor.setPartyDistritorName(customerSalesmanVendor.getPartyDistributor().getDistributorName());
            String address = "";
            List<PostalAddress> postalAddresses = customerSalesmanVendor.getPartyCustomer().getPostalAddress();
            for (PostalAddress postalAddress: postalAddresses){
                address += postalAddress.getAddress() + "; ";
            }
            customerSalesmanVendor.setAddress(address);

        }
        */
        Page<RetailOutletSalesmanVendor> retailOutletSalesmanVendorPage = retailOutletSalesmanVendorPagingRepo.findByPartySalesmanAndThruDate(partySalesman,null,pageable);
        for (RetailOutletSalesmanVendor retailOutletSalesmanVendor: retailOutletSalesmanVendorPage ) {
            retailOutletSalesmanVendor.setRetailOutletName(retailOutletSalesmanVendor.getPartyRetailOutlet().getRetailOutletName());
            retailOutletSalesmanVendor.setRetailOutletCode(retailOutletSalesmanVendor.getPartyRetailOutlet().getRetailOutletCode());
            retailOutletSalesmanVendor.setPartyDistritorName(retailOutletSalesmanVendor.getPartyDistributor().getDistributorName());
            String address = "";
            List<PostalAddress> postalAddresses = retailOutletSalesmanVendor.getPartyRetailOutlet().getPostalAddress();
            for (PostalAddress postalAddress: postalAddresses){
                address += postalAddress.getAddress() + "; ";
            }
            retailOutletSalesmanVendor.setAddress(address);

        }

        return ResponseEntity.ok().body(retailOutletSalesmanVendorPage);
    }

    @PostMapping("/add-customer-distributor-salesman/{partyId}")
    public ResponseEntity<?> addCustomerDistributorSalesman(@PathVariable String partyId, @RequestBody CustomerDistributorSalesmanInputModel input){
        log.info("addCustomerDistributorSalesman {}",partyId);
        PartyCustomer partyCustomer = partyCustomerRepo.findByPartyId(UUID.fromString(input.getPartyCustomerId()));
        PartyDistributor partyDistributor = partyDistributorRepo.findByPartyId(UUID.fromString(input.getPartyDistributorId()));
        PartySalesman partySalesman = partySalesmanService.findById(UUID.fromString(partyId));
        List<CustomerSalesmanVendor> lst = customerSalesmanVendorRepo.findAllByPartySalesmanAndPartyCustomerAndPartyDistributorAndThruDate(partySalesman, 
        		partyCustomer, partyDistributor, null);
        if(lst != null && lst.size() > 0){
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

    @PostMapping("/add-retail-outlet-distributor-salesman/{partyId}")
    public ResponseEntity<?> addRetailOutletDistributorSalesman(@PathVariable String partyId, @RequestBody RetailOutletSalesmanDistributorInputModel input){
        log.info("addRetailOutletDistributorSalesman {}",partyId);
        PartyRetailOutlet partyRetailOutlet = retailOutletRepo.findByPartyId(UUID.fromString(input.getPartyRetailOutletId()));
        PartyDistributor partyDistributor = partyDistributorRepo.findByPartyId(UUID.fromString(input.getPartyDistributorId()));
        PartySalesman partySalesman = partySalesmanService.findById(UUID.fromString(partyId));
        List<RetailOutletSalesmanVendor> lst = retailOutletSalesmanVendorRepo.findAllByPartySalesmanAndPartyRetailOutletAndPartyDistributorAndThruDate(partySalesman,
                partyRetailOutlet, partyDistributor, null);
        if(lst != null && lst.size() > 0){
            return ResponseEntity.status(HttpStatus.ALREADY_REPORTED).body("DUPLICATE");
        }
        RetailOutletSalesmanVendor retailOutletSalesmanVendor = new RetailOutletSalesmanVendor();
        retailOutletSalesmanVendor.setPartyRetailOutlet(partyRetailOutlet);
        retailOutletSalesmanVendor.setPartySalesman(partySalesman);
        retailOutletSalesmanVendor.setPartyDistributor(partyDistributor);
        retailOutletSalesmanVendor.setFromDate(new Date());// take current date-time
        retailOutletSalesmanVendorRepo.save(retailOutletSalesmanVendor);
        return ResponseEntity.ok(input);
    }

    @GetMapping("/delete-customer-distributor-salesman/{Id}")
    public void deleteCustomerDistributorSalesman (@PathVariable String Id){
        CustomerSalesmanVendor customerSalesmanVendor = customerSalesmanVendorRepo.findByCustomerSalesmanVendorId(UUID.fromString(Id));
        //customerSalesmanVendorRepo.delete(customerSalesmanVendor);// do not delete physically 
        customerSalesmanVendor.setThruDate(new Date());// set thru_date by current date-time
        customerSalesmanVendorRepo.save(customerSalesmanVendor);
    }

    @GetMapping("/delete-retail-outlet-distributor-salesman/{Id}")
    public void deleteRetailOutletDistributorSalesman (@PathVariable String Id){
        RetailOutletSalesmanVendor retailOutletSalesmanVendor = retailOutletSalesmanVendorRepo.findByRetailOutletSalesmanVendorId(UUID.fromString(Id));
        //customerSalesmanVendorRepo.delete(customerSalesmanVendor);// do not delete physically
        retailOutletSalesmanVendor.setThruDate(new Date());// set thru_date by current date-time
        retailOutletSalesmanVendorRepo.save(retailOutletSalesmanVendor);
    }




}	

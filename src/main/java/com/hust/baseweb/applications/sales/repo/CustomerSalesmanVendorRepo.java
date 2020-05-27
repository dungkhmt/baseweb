package com.hust.baseweb.applications.sales.repo;

import com.hust.baseweb.applications.customer.entity.PartyCustomer;
import com.hust.baseweb.applications.customer.entity.PartyDistributor;
import com.hust.baseweb.applications.sales.entity.CustomerSalesmanVendor;
import com.hust.baseweb.applications.sales.entity.PartySalesman;
import org.springframework.data.repository.CrudRepository;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public interface CustomerSalesmanVendorRepo extends CrudRepository<CustomerSalesmanVendor, UUID> {

    CustomerSalesmanVendor findByCustomerSalesmanVendorId(UUID customerSalesmanVendorId);

    List<CustomerSalesmanVendor> findAllByPartySalesmanAndPartyCustomerAndPartyDistributorAndThruDate(
        PartySalesman partySalesman,
        PartyCustomer partyCustomer,
        PartyDistributor partyDistributor,
        Date thruDate);
}

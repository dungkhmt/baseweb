package com.hust.baseweb.applications.sales.service;

import com.hust.baseweb.applications.customer.entity.PartyCustomer;
import com.hust.baseweb.applications.sales.entity.CustomerSalesman;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface CustomerSalesmanService {
    List<PartyCustomer> getCustomersOfSalesman(UUID partySalesmanId);

    CustomerSalesman save(UUID partyCustomerId, UUID partySalesmanId);
}

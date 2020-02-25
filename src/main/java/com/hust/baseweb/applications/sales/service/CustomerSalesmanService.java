package com.hust.baseweb.applications.sales.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.hust.baseweb.applications.customer.entity.PartyCustomer;
import com.hust.baseweb.applications.sales.entity.CustomerSalesman;

@Service
public interface CustomerSalesmanService {
	List<PartyCustomer> getCustomersOfSalesman(UUID partySalesmanId);
	CustomerSalesman save(UUID partyCustomerId, UUID partySalesmanId);
}

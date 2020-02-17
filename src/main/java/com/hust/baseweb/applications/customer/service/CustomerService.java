package com.hust.baseweb.applications.customer.service;

import org.springframework.stereotype.Service;

import com.hust.baseweb.applications.customer.entity.PartyCustomer;
import com.hust.baseweb.applications.customer.model.CreateCustomerInputModel;

@Service
public interface CustomerService {
	public PartyCustomer save(CreateCustomerInputModel input);
	
}

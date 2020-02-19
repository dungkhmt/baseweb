package com.hust.baseweb.applications.customer.service;

import com.hust.baseweb.applications.customer.entity.PartyCustomer;
import com.hust.baseweb.applications.customer.model.CreateCustomerInputModel;
import org.springframework.stereotype.Service;

@Service
public interface CustomerService {
    public PartyCustomer save(CreateCustomerInputModel input);

}

package com.hust.baseweb.applications.customer.service;

import java.util.List;

import com.hust.baseweb.applications.customer.entity.PartyCustomer;
import com.hust.baseweb.applications.customer.model.CreateCustomerInputModel;
import com.hust.baseweb.entity.PartyType;

import org.springframework.stereotype.Service;

@Service
public interface CustomerService {
    public PartyCustomer save(CreateCustomerInputModel input);
    List<PartyCustomer> findDistributors();
    List<PartyCustomer> findRetailOutlers();
}

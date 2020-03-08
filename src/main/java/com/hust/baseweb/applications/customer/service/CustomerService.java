package com.hust.baseweb.applications.customer.service;

import com.hust.baseweb.applications.customer.entity.PartyCustomer;
import com.hust.baseweb.applications.customer.model.CreateCustomerInputModel;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CustomerService {
    public PartyCustomer save(CreateCustomerInputModel input);

    List<PartyCustomer> findDistributors();

    List<PartyCustomer> findRetailOutlers();
}

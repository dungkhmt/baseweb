package com.hust.baseweb.applications.customer.service;

import com.hust.baseweb.applications.customer.entity.PartyCustomer;
import com.hust.baseweb.applications.customer.model.CreateCustomerInputModel;
import com.hust.baseweb.applications.customer.model.CreateDistributorInputModel;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CustomerService {
    PartyCustomer save(CreateCustomerInputModel input);

    PartyCustomer save(CreateDistributorInputModel input);

    List<PartyCustomer> findDistributors();

    List<PartyCustomer> findRetailOutlers();
}

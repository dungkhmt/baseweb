package com.hust.baseweb.applications.customer.service;

import com.hust.baseweb.applications.customer.entity.PartyRetailOutlet;
import com.hust.baseweb.applications.customer.model.CreateRetailOutletInputModel;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface RetailOutletService {
    PartyRetailOutlet save(CreateRetailOutletInputModel input);
    List<PartyRetailOutlet> findAll();

}

package com.hust.baseweb.applications.order.service;

import com.hust.baseweb.applications.customer.entity.PartyCustomer;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public interface PartyCustomerService {
    List<PartyCustomer> getListPartyCustomers();
}

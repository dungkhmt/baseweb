package com.hust.baseweb.applications.order.service;

import com.hust.baseweb.applications.customer.model.PartyCustomerModel;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public interface PartyCustomerService {

    List<PartyCustomerModel> getListPartyCustomers();
}

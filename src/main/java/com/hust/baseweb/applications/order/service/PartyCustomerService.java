package com.hust.baseweb.applications.order.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hust.baseweb.applications.order.entity.PartyCustomer;

@Service
public interface PartyCustomerService {
	public List<PartyCustomer> getListPartyCustomers();
}

package com.hust.baseweb.applications.order.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hust.baseweb.applications.order.entity.PartyCustomer;
import com.hust.baseweb.applications.order.repo.PartyCustomerRepo;

@Service
public class PartyCustomerServiceImpl implements PartyCustomerService {
	@Autowired
	private PartyCustomerRepo partyCustomerRepo;
	
	@Override
	public List<PartyCustomer> getListPartyCustomers() {
		// TODO Auto-generated method stub
		return partyCustomerRepo.findAll();
	}

}

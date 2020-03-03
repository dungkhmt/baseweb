package com.hust.baseweb.applications.salesroutes.service;

import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hust.baseweb.applications.customer.entity.PartyCustomer;
import com.hust.baseweb.applications.order.repo.PartyCustomerRepo;
import com.hust.baseweb.applications.order.service.PartyCustomerService;
import com.hust.baseweb.applications.salesroutes.entity.SalesmanCheckinHistory;
import com.hust.baseweb.applications.salesroutes.repo.SalesmanCheckinHistoryRepo;
import com.hust.baseweb.entity.UserLogin;

@Service
public class SalesmanCheckinHistoryServiceImpl implements
		SalesmanCheckinHistoryService {

	@Autowired
	private SalesmanCheckinHistoryRepo salesmanCheckinHistoryRepo;
	
	@Autowired
	private PartyCustomerService partyCustomerService;
	
	@Autowired
	private PartyCustomerRepo partyCustomerRepo;
	
	@Override
	public SalesmanCheckinHistory save(UserLogin userLogin, UUID partyCustomerId, String checkinAction, String location) {
		// TODO Auto-generated method stub
		PartyCustomer partyCustomer = partyCustomerRepo.findByPartyId(partyCustomerId);
		
		SalesmanCheckinHistory sch = new SalesmanCheckinHistory();
		sch.setUserLogin(userLogin);
		sch.setPartyCustomer(partyCustomer);
		sch.setTimePoint(new Date());
		sch.setCheckinAction(checkinAction);
		sch.setLocation(location);
		sch = salesmanCheckinHistoryRepo.save(sch);
		return sch;
	}

}

package com.hust.baseweb.applications.customer.service;


import java.util.Date;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hust.baseweb.applications.customer.entity.PartyContactMechPurpose;
import com.hust.baseweb.applications.customer.entity.PartyCustomer;
import com.hust.baseweb.applications.customer.model.CreateCustomerInputModel;
import com.hust.baseweb.applications.customer.repo.CustomerRepo;
import com.hust.baseweb.applications.customer.repo.PartyContactMechPurposeRepo;
import com.hust.baseweb.applications.geo.entity.GeoPoint;
import com.hust.baseweb.applications.geo.entity.PostalAddress;
import com.hust.baseweb.applications.geo.repo.GeoPointRepo;
import com.hust.baseweb.applications.geo.repo.PostalAddressRepo;
import com.hust.baseweb.entity.Party;
import com.hust.baseweb.repo.PartyRepo;

@Service
public class CustomerServiceImpl implements CustomerService {
	public static Logger LOG = LoggerFactory.getLogger(CustomerServiceImpl.class);
	
	@Autowired
	private CustomerRepo customerRepo;
	
	@Autowired
	private GeoPointRepo geoPointRepo;
	
	@Autowired
	private PostalAddressRepo postalAddressRepo;
	
	@Autowired
	private PartyRepo partyRepo;
	
	@Autowired
	private PartyContactMechPurposeRepo partyContactMechPurposeRepo;
	
	@Override
	@Transactional
	public PartyCustomer save(CreateCustomerInputModel input) {
		// TODO Auto-generated method stub
		
		UUID partyId = UUID.randomUUID();
		Party party = new Party();
		party.setPartyId(partyId);
		partyRepo.save(party);
		LOG.info("save party " + partyId);
		
		PartyCustomer customer = new PartyCustomer();
		customer.setPartyId(partyId);
		//customer.setParty(party);
		customer.setCustomerName(input.getCustomerName());
		LOG.info("save, prepare save customer partyId = " + customer.getPartyId());
		customerRepo.save(customer);
		
		GeoPoint geoPoint = new GeoPoint();
		UUID geoPointId = UUID.randomUUID();
		geoPoint.setLatitude(input.getLatitude());
		geoPoint.setLongitude(input.getLongitude());
		geoPoint.setGeoPointId(geoPointId);
		geoPointRepo.save(geoPoint);
		
		UUID contactMechId = UUID.randomUUID();
		PostalAddress address = new PostalAddress();
		address.setContactMechId(contactMechId);
		address.setGeoPoint(geoPoint);
		postalAddressRepo.save(address);	
		
		
		
		// write to PartyContactMech
		PartyContactMechPurpose pcmp = new PartyContactMechPurpose();
		pcmp.setContactMechId(contactMechId);
		pcmp.setPartyId(partyId);
		pcmp.setContactMechPurposeTypeId("PRIMARY_LOCATION");
		pcmp.setFromDate(new Date());
		partyContactMechPurposeRepo.save(pcmp);
		
		return customer;
	}

}

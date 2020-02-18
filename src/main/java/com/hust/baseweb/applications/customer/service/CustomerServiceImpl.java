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
import com.hust.baseweb.entity.PartyType.PartyTypeEnum;
import com.hust.baseweb.entity.Status.StatusEnum;
import com.hust.baseweb.repo.PartyRepo;
import com.hust.baseweb.repo.PartyTypeRepo;
import com.hust.baseweb.repo.StatusRepo;

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
	private PartyTypeRepo partyTypeRepo;
	@Autowired
	private StatusRepo statusRepo;
	
	@Autowired
	private PartyContactMechPurposeRepo partyContactMechPurposeRepo;
	
	@Override
	@Transactional
	public PartyCustomer save(CreateCustomerInputModel input) {
		// TODO Auto-generated method stub
		
		//UUID partyId = UUID.randomUUID();
		//Party party = new Party();
		//party.setPartyId(partyId);// KHONG WORK vi partyId khi insert vao DB se duoc sinh tu dong, no se khac voi partyId sinh ra boi SPRING
		Party party = new Party(null, partyTypeRepo.getOne(PartyTypeEnum.PERSON.name()), "",
                statusRepo.findById(StatusEnum.PARTY_ENABLED.name()).get(), false, null);
		
		partyRepo.save(party);
		
		UUID partyId = party.getPartyId();
		LOG.info("save party " + partyId);
		
		PartyCustomer customer = new PartyCustomer();
		customer.setPartyId(partyId);
		//customer.setParty(party);
		customer.setCustomerName(input.getCustomerName());
		LOG.info("save, prepare save customer partyId = " + customer.getPartyId());
		customerRepo.save(customer);
		
		GeoPoint geoPoint = new GeoPoint();
		//UUID geoPointId = UUID.randomUUID();
		geoPoint.setLatitude(input.getLatitude());
		geoPoint.setLongitude(input.getLongitude());
		//geoPoint.setGeoPointId(geoPointId);// KHONG WORK vi khi save vao DB thi geoPointId se duoc sinh voi DB engine
		geoPoint = geoPointRepo.save(geoPoint);
		UUID geoPointId = geoPoint.getGeoPointId();
		
		//UUID contactMechId = UUID.randomUUID();
		PostalAddress address = new PostalAddress();
		//address.setContactMechId(contactMechId);// KHONG WORL vi contactMechId se duoc sinh tu dong boi DB uuid_generate_v1()
		address.setGeoPoint(geoPoint);
		address = postalAddressRepo.save(address);	
		UUID contactMechId = address.getContactMechId();
		
		
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

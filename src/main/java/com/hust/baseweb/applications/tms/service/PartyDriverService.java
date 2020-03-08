package com.hust.baseweb.applications.tms.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.hust.baseweb.applications.tms.entity.PartyDriver;
import com.hust.baseweb.applications.tms.model.driver.CreateDriverInputModel;

@Service
public interface PartyDriverService {
	PartyDriver save(CreateDriverInputModel input);
	List<PartyDriver> findAll();
	
	PartyDriver findByPartyId(UUID partyId);
}

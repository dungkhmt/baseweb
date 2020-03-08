package com.hust.baseweb.applications.tms.service;

import java.util.List;
import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hust.baseweb.applications.tms.entity.PartyDriver;
import com.hust.baseweb.applications.tms.model.driver.CreateDriverInputModel;
import com.hust.baseweb.applications.tms.repo.PPartyDriverRepo;
import com.hust.baseweb.applications.tms.repo.PartyDriverRepo;
import com.hust.baseweb.entity.Party;
import com.hust.baseweb.model.PersonModel;
import com.hust.baseweb.service.PartyService;
import com.hust.baseweb.service.UserService;

@Service
public class PartyDriverServiceImpl implements PartyDriverService {

	@Autowired
	private PPartyDriverRepo pPartyDriverRepo;
	
	@Autowired
	private PartyDriverRepo partyDriverRepo;
	@Autowired
	private UserService userService;

	@Override
	@Transactional
	public PartyDriver save(CreateDriverInputModel input) {
		// TODO:
		PersonModel personModel = new PersonModel();
		personModel.setBirthDate(input.getBirthDate());
		personModel.setFirstName(input.getFirstName());
		personModel.setMiddleName(input.getMiddleName());
		personModel.setLastName(input.getLastName());
		personModel.setUserName(input.getUserName());
		personModel.setPassword(input.getPassword());
		personModel.setPartyCode(input.getPartyCode());
		personModel.setRoles(input.getRoles());
		try{
			Party party = userService.save(personModel);
			PartyDriver partyDriver = new PartyDriver();
			partyDriver.setPartyId(party.getPartyId());
			partyDriver = partyDriverRepo.save(partyDriver);
			return partyDriver;
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return null;
	}

	@Override
	public List<PartyDriver> findAll() {
		// TODO Auto-generated method stub
		return partyDriverRepo.findAll();
	}

	@Override
	public PartyDriver findByPartyId(UUID partyId) {
		// TODO Auto-generated method stub
		PartyDriver partyDriver = partyDriverRepo.findByPartyId(partyId);
		return partyDriver;
	}

}

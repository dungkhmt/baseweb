package com.hust.baseweb.service;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hust.baseweb.entity.Party;
import com.hust.baseweb.entity.PartyType;
import com.hust.baseweb.repo.PartyRepo;
import com.hust.baseweb.repo.PartyTypeRepo;

@Service
public class PartyServiceImpl implements PartyService {
	@Autowired
	PartyRepo partyRepo;
	@Autowired
	PartyTypeRepo partyTypeRepo;
	
	@Override
	public Party save(String partyTypeId) {
		// TODO Auto-generated method stub
		PartyType pt = partyTypeRepo.getOne(partyTypeId);
		UUID uuid = UUID.randomUUID();
		Party p = new Party();
		p.setPartyId(uuid);
		p.setType(pt);
		return partyRepo.save(p);
	}

}

package com.hust.baseweb.applications.tms.service;

import com.hust.baseweb.applications.tms.entity.PartyDriver;
import com.hust.baseweb.applications.tms.model.DriverModel;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface PartyDriverService {
	PartyDriver save(DriverModel.InputCreate input);

	List<PartyDriver> findAll();

	PartyDriver findByPartyId(UUID partyId);
}

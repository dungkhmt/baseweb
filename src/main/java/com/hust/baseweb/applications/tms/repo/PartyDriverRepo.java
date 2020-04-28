package com.hust.baseweb.applications.tms.repo;

import com.hust.baseweb.applications.tms.entity.PartyDriver;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import java.awt.print.Pageable;
import java.util.UUID;

public interface PartyDriverRepo extends JpaRepository<PartyDriver, UUID> {
	PartyDriver findByPartyId(UUID partyId);
	//Page<PartyDriver> findAll(Pageable page);
}

package com.hust.baseweb.applications.tms.repo;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.hust.baseweb.applications.tms.entity.PartyDriver;
import java.util.UUID;

public interface PPartyDriverRepo extends PagingAndSortingRepository<PartyDriver, UUID> {
	
}

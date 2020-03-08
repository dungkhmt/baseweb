package com.hust.baseweb.applications.tms.repo;

import com.hust.baseweb.applications.tms.entity.PartyDriver;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.UUID;

public interface PPartyDriverRepo extends PagingAndSortingRepository<PartyDriver, UUID> {

}

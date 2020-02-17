package com.hust.baseweb.applications.customer.repo;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.hust.baseweb.applications.customer.entity.CompositePartyContactMechPurposeId;
import com.hust.baseweb.applications.customer.entity.PartyContactMechPurpose;

public interface PartyContactMechPurposeRepo extends
		PagingAndSortingRepository<PartyContactMechPurpose, CompositePartyContactMechPurposeId> {

}

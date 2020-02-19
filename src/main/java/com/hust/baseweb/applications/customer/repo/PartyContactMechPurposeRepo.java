package com.hust.baseweb.applications.customer.repo;

import com.hust.baseweb.applications.customer.entity.CompositePartyContactMechPurposeId;
import com.hust.baseweb.applications.customer.entity.PartyContactMechPurpose;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface PartyContactMechPurposeRepo extends
        PagingAndSortingRepository<PartyContactMechPurpose, CompositePartyContactMechPurposeId> {

}

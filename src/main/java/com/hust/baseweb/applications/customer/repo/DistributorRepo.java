package com.hust.baseweb.applications.customer.repo;

import com.hust.baseweb.applications.customer.entity.PartyDistributor;
import com.hust.baseweb.entity.PartyType;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.UUID;

public interface DistributorRepo extends PagingAndSortingRepository<PartyDistributor, UUID> {
    List<PartyDistributor> findAll();

    List<PartyDistributor> findByPartyType(PartyType partyType);
}

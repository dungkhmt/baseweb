package com.hust.baseweb.applications.order.repo;

import com.hust.baseweb.applications.customer.entity.PartyDistributor;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.UUID;

public interface PartyDistributorRepo extends CrudRepository<PartyDistributor, UUID> {
    PartyDistributor findByPartyId(UUID partyId);

    List<PartyDistributor> findAllByPartyIdIn(List<UUID> ids);
}

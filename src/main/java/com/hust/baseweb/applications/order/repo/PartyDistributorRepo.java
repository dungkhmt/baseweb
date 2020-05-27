package com.hust.baseweb.applications.order.repo;

import com.hust.baseweb.applications.customer.entity.PartyDistributor;
import com.hust.baseweb.entity.PartyType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface PartyDistributorRepo extends JpaRepository<PartyDistributor, UUID> {

    PartyDistributor findByPartyId(UUID partyId);

    List<PartyDistributor> findAllByPartyIdIn(List<UUID> ids);

    List<PartyDistributor> findByPartyType(PartyType partyType);

    List<PartyDistributor> findAllByPartyIdNotIn(List<UUID> partyIds);

    Page<PartyDistributor> findAllByPartyIdIn(List<UUID> partyIds, Pageable page);
}

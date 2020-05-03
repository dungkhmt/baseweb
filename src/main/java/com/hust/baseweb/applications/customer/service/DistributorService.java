package com.hust.baseweb.applications.customer.service;

import com.hust.baseweb.applications.customer.entity.PartyDistributor;
import com.hust.baseweb.applications.customer.model.CreateDistributorInputModel;
import com.hust.baseweb.applications.customer.model.DetailDistributorModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface DistributorService {
    PartyDistributor save(CreateDistributorInputModel input);

    List<PartyDistributor> findDistributors();
    PartyDistributor findByPartyId(UUID partyId);
    List<PartyDistributor> findAllByPartyIdIn(List<UUID> partyIds);
    Page<PartyDistributor> findAllByPartyIdIn(List<UUID> partyIds, Pageable page);

    DetailDistributorModel getDistributorDetail(UUID partyDistributorId);
    List<PartyDistributor> getDistributorCandidates(UUID partyRetailOutletId);
}

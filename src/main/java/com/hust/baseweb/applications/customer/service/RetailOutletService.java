package com.hust.baseweb.applications.customer.service;

import com.hust.baseweb.applications.customer.entity.PartyRetailOutlet;
import com.hust.baseweb.applications.customer.model.CreateRetailOutletInputModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface RetailOutletService {
    PartyRetailOutlet save(CreateRetailOutletInputModel input);
    List<PartyRetailOutlet> findAll();
    Page<PartyRetailOutlet> findAll(Pageable page);

    List<PartyRetailOutlet> findByPartyIdIn(List<UUID> partyIds);
    Page<PartyRetailOutlet> findByPartyIdIn(List<UUID> partyIds, Pageable page);

    List<PartyRetailOutlet> getRetailOutletCandidates(UUID partyDistributorId);
}

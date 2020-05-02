package com.hust.baseweb.applications.customer.repo;

import com.hust.baseweb.applications.customer.entity.PartyRetailOutlet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface PartyRetailOutletRepo extends JpaRepository<PartyRetailOutlet, UUID> {
    List<PartyRetailOutlet> findAll();
    Page<PartyRetailOutlet> findAll(Pageable page);
    PartyRetailOutlet findByPartyId(UUID partyId);
    List<PartyRetailOutlet> findAllByPartyIdIn(List<UUID> partyIds);
    List<PartyRetailOutlet> findAllByPartyIdNotIn(List<UUID> partyIds);
    Page<PartyRetailOutlet> findAllByPartyIdIn(List<UUID> partyIds, Pageable page);
}

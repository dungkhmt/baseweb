package com.hust.baseweb.applications.customer.repo;

import com.hust.baseweb.applications.customer.entity.PartyRetailOutlet;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.UUID;

public interface RetailOutletPagingRepo extends PagingAndSortingRepository<PartyRetailOutlet, UUID> {

    List<PartyRetailOutlet> findAllByRetailOutletCodeIn(List<String> retailOutletCodes);

    PartyRetailOutlet findByPartyId(UUID partyId);


    //List<PartyCustomer> findAllByRetailOutletCode(String retailOutletCode);

    //List<PartyCustomer> findAllByPartyIdIn(List<UUID> partyIds);
}

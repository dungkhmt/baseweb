package com.hust.baseweb.service;

import com.hust.baseweb.entity.Party;
import com.hust.baseweb.entity.PartyType;
import com.hust.baseweb.repo.PartyRepo;
import com.hust.baseweb.repo.PartyTypeRepo;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class PartyServiceImpl implements PartyService {
    PartyRepo partyRepo;
    PartyTypeRepo partyTypeRepo;

    @Override
    public Party save(String partyTypeId) {

        PartyType partyType = partyTypeRepo.getOne(partyTypeId);
        UUID uuid = UUID.randomUUID();
        Party party = new Party();
        party.setPartyId(uuid);
        party.setType(partyType);
        return partyRepo.save(party);
    }

}

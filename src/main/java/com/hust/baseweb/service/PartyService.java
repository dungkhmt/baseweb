package com.hust.baseweb.service;

import com.hust.baseweb.entity.Party;

public interface PartyService {
    Party save(String partyType);

    Party disableParty(String partyId);
}

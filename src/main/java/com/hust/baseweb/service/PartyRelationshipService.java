package com.hust.baseweb.service;

import com.hust.baseweb.entity.Party;
import com.hust.baseweb.entity.PartyRelationship;
import com.hust.baseweb.entity.RoleType;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public interface PartyRelationshipService {
    public PartyRelationship save(PartyRelationship partyRelationship);

     public List<PartyRelationship> findAllByFromPartyAndRoleTypeAndThruDate(Party fromParty,
                                                                            RoleType roleType,
                                                                            Date thruDate);

    public List<PartyRelationship> findAllByToPartyAndRoleTypeAndThruDate(Party toParty,
                                                                          RoleType roleType,
                                                                          Date thruDate);

    public List<PartyRelationship> findAllByFromPartyAndToPartyAndRoleTypeAndThruDate(Party fromParty,
                                                                                      Party toParty,
                                                                                      RoleType roleType,
                                                                                      Date thruDate);

}

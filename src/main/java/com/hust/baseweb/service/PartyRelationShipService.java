package com.hust.baseweb.service;

import com.hust.baseweb.entity.Party;
import com.hust.baseweb.entity.PartyRelationShip;
import com.hust.baseweb.entity.RoleType;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public interface PartyRelationShipService {
    public PartyRelationShip save(PartyRelationShip partyRelationShip);
    public List<PartyRelationShip> findAllByFromPartyAndRoleTypeAndThruDate(Party fromParty, RoleType roleType, Date thruDate);
    public List<PartyRelationShip> findAllByToPartyAndRoleTypeAndThruDate(Party toParty, RoleType roleType, Date thruDate);
    public List<PartyRelationShip> findAllByFromPartyAndToPartyAndRoleTypeAndThruDate(Party fromParty, Party toParty, RoleType roleType, Date thruDate);

}

package com.hust.baseweb.repo;

import com.hust.baseweb.entity.Party;
import com.hust.baseweb.entity.PartyRelationShip;
import com.hust.baseweb.entity.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public interface PartyRelationShipRepo extends JpaRepository<PartyRelationShip, UUID> {
    public PartyRelationShip save(PartyRelationShip partyRelationShip);
    public List<PartyRelationShip> findAllByFromPartyAndRoleTypeAndThruDate(Party fromParty, RoleType roleType, Date thruDate);
    public List<PartyRelationShip> findAllByToPartyAndRoleTypeAndThruDate(Party toParty, RoleType roleType, Date thruDate);
    public List<PartyRelationShip> findAllByFromPartyAndToPartyAndRoleTypeAndThruDate(Party fromParty, Party toParty, RoleType roleType, Date thruDate);
}

package com.hust.baseweb.service;

import com.hust.baseweb.entity.Party;
import com.hust.baseweb.entity.PartyRelationShip;
import com.hust.baseweb.entity.RoleType;
import com.hust.baseweb.repo.PartyRelationShipRepo;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))

public class PartyRelationShipServiceImpl implements PartyRelationShipService{
    private PartyRelationShipRepo partyRelationShipRepo;

    @Override
    public PartyRelationShip save(PartyRelationShip partyRelationShip) {
        return partyRelationShipRepo.save(partyRelationShip);
    }

    @Override
    public List<PartyRelationShip> findAllByFromPartyAndRoleTypeAndThruDate(Party fromParty, RoleType roleType, Date thruDate) {
        return partyRelationShipRepo.findAllByFromPartyAndRoleTypeAndThruDate(fromParty, roleType, thruDate);
    }

    @Override
    public List<PartyRelationShip> findAllByToPartyAndRoleTypeAndThruDate(Party toParty, RoleType roleType, Date thruDate) {
        return partyRelationShipRepo.findAllByToPartyAndRoleTypeAndThruDate(toParty, roleType, thruDate);
    }

    @Override
    public List<PartyRelationShip> findAllByFromPartyAndToPartyAndRoleTypeAndThruDate(Party fromParty, Party toParty, RoleType roleType, Date thruDate) {
        return partyRelationShipRepo.findAllByFromPartyAndToPartyAndRoleTypeAndThruDate(fromParty,toParty,roleType,thruDate);
    }
}

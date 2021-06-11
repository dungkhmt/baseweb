package com.hust.baseweb.applications.backlog.model;

import com.hust.baseweb.entity.Party;
import com.hust.baseweb.entity.PartyType;
import com.hust.baseweb.entity.Person;
import com.hust.baseweb.entity.UserLogin;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserLoginReduced {

    private String userLoginId;
    private UUID partyId;
    private String partyCode;
    private String partyName;
    private PartyType partyType;
    private boolean enabled;
    private String clientToken;
    private Date disabledDateTime;
    private Person person;

    public UserLoginReduced(UserLogin user) {
        userLoginId = user.getUserLoginId();
        Party party = user.getParty();
        partyId = party.getPartyId();
        partyCode = party.getPartyCode();
        partyName = party.getName();
        partyType = party.getType();
        enabled = user.isEnabled();
        clientToken = user.getClientToken();
        disabledDateTime = user.getDisabledDateTime();
    }

    public UserLoginReduced(UserLogin user, Person person) {
        userLoginId = user.getUserLoginId();
        Party party = user.getParty();
        partyId = party.getPartyId();
        partyCode = party.getPartyCode();
        partyName = party.getName();
        partyType = party.getType();
        enabled = user.isEnabled();
        clientToken = user.getClientToken();
        disabledDateTime = user.getDisabledDateTime();
        this.person = person;
    }
}

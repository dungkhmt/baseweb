package com.hust.baseweb.applications.tms.entity;

import com.hust.baseweb.entity.Party;
import com.hust.baseweb.entity.Person;
import com.hust.baseweb.entity.UserLogin;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Optional;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PartyDriver {

    @Id
    @Column(name = "party_id")
    private UUID partyId;

    @JoinColumn(name = "party_id", referencedColumnName = "party_id")
    @OneToOne(fetch = FetchType.EAGER)
    private Person person;

    @JoinColumn(name = "party_id", referencedColumnName = "party_id")
    @OneToOne(fetch = FetchType.EAGER)
    private Party party;

    //@JoinColumn(name = "party_id", referencedColumnName = "party_id")
    //@ManyToOne(fetch = FetchType.EAGER)
    //private UserLogin userLogin;

    public Model toModel() {
        return new Model(
            partyId.toString(),
            Optional.ofNullable(party).map(Party::getUserLogin).map(UserLogin::getUserLoginId).orElse(null)
        );
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Model {

        private String partyId;
        private String userLoginId;
    }

}

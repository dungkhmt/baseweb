package com.hust.baseweb.applications.sales.entity;

import com.hust.baseweb.entity.Person;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.UUID;


@Entity
@Getter
@Setter

public class PartySalesman {
    @Id
    @Column(name = "party_id")
    private UUID partyId;

    @JoinColumn(name = "party_id", referencedColumnName = "party_id")
    @OneToOne(fetch = FetchType.EAGER)
    private Person person;

    //@JoinColumn(name="party_id", referencedColumnName="party_id")
    //@ManyToOne(fetch=FetchType.EAGER)
    //private UserLogin userLogin;

}

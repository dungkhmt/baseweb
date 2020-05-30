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
    @OneToOne(fetch = FetchType.LAZY)
    private Person person;

    //@JoinColumn(name="party_id", referencedColumnName="party_id")
    //@ManyToOne(fetch=FetchType.LAZY)
    //private UserLogin userLogin;

    @Transient
    private String name;

    @Transient
    private String userName;

}

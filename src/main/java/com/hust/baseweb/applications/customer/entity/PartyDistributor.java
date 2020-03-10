package com.hust.baseweb.applications.customer.entity;

import java.util.List;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import com.hust.baseweb.applications.geo.entity.PostalAddress;
import com.hust.baseweb.entity.PartyType;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PartyDistributor {
    @Id
    @Column(name = "party_id")
    private UUID partyId;

    //@JoinColumn(name = "party_id", referencedColumnName = "party_id")
    //@OneToOne(fetch = FetchType.EAGER)
    //private Party party;

    @JoinColumn(name = "party_type_id", referencedColumnName = "party_type_id")
    @ManyToOne(fetch = FetchType.EAGER)
    private PartyType partyType;

    @Column(name = "distributor_code")
    private String distributorCode;

    @Column(name = "distributor_name")
    private String distributorName;

    @JoinTable(name = "PartyContactMechPurpose", inverseJoinColumns = @JoinColumn(name = "contact_mech_id", referencedColumnName = "contact_mech_id"),
            joinColumns = @JoinColumn(name = "party_id", referencedColumnName = "party_id"))
    @OneToMany(fetch = FetchType.LAZY)
    private List<PostalAddress> postalAddress;

}

package com.hust.baseweb.applications.customer.entity;

import com.hust.baseweb.applications.geo.entity.PostalAddress;
import com.hust.baseweb.entity.PartyType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PartyRetailOutlet {

    @Id
    @Column(name = "party_id")
    private UUID partyId;

    @JoinColumn(name = "party_type_id", referencedColumnName = "party_type_id")
    @ManyToOne(fetch = FetchType.EAGER)
    private PartyType partyType;

    @Column(name = "retail_outlet_code")
    private String retailOutletCode;

    @Column(name = "retail_outlet_name")
    private String retailOutletName;

    @JoinTable(name = "PartyContactMechPurpose",
               inverseJoinColumns = @JoinColumn(name = "contact_mech_id", referencedColumnName = "contact_mech_id"),
               joinColumns = @JoinColumn(name = "party_id", referencedColumnName = "party_id"))
    @OneToMany(fetch = FetchType.LAZY)
    private List<PostalAddress> postalAddress;

    @Transient
    private String type;

}

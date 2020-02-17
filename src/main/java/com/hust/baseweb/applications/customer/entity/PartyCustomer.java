package com.hust.baseweb.applications.customer.entity;

import java.util.List;
import java.util.UUID;

import javax.persistence.*;

import com.hust.baseweb.applications.geo.entity.PostalAddress;
import com.hust.baseweb.entity.Party;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter

public class PartyCustomer {
	@Id
    @Column(name="party_id")
    private UUID partyId;
	
	//@JoinColumn(name = "party_id", referencedColumnName = "party_id")
    //@OneToOne(fetch = FetchType.EAGER)
    //private Party party;
	
	@Column(name="customer_name")
	private String customerName;
	
	@JoinTable(name="PartyContactMechPurpose", inverseJoinColumns=@JoinColumn(name="contact_mech_id", referencedColumnName="contact_mech_id"),
				joinColumns=@JoinColumn(name="party_id", referencedColumnName="party_id"))
		@OneToMany(fetch=FetchType.LAZY)
	private List<PostalAddress> postalAddress;
}

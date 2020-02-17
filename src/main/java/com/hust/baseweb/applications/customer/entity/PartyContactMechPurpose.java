package com.hust.baseweb.applications.customer.entity;

import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@IdClass(CompositePartyContactMechPurposeId.class)
public class PartyContactMechPurpose {
	@Id
	@Column(name="party_id")
	private UUID partyId;
	
	@Id
	@Column(name="contact_mech_id")
	private UUID contactMechId;
	
	@Id
	@Column(name="contact_mech_purpose_type_id")
	private String contactMechPurposeTypeId;
	
	@Id
	@Column(name="from_date")
	private Date fromDate;
	
	@Column(name="thru_date")
	private Date thruDate;
	
}

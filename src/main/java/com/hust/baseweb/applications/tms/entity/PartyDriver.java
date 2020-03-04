package com.hust.baseweb.applications.tms.entity;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import com.hust.baseweb.entity.Person;
import com.hust.baseweb.entity.UserLogin;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class PartyDriver {
	@Id
    @Column(name = "party_id")
    private UUID partyId;
	
	 @JoinColumn(name = "party_id", referencedColumnName = "party_id")
	 @OneToOne(fetch = FetchType.EAGER)
	 private Person person;
	
	 //@JoinColumn(name = "party_id", referencedColumnName = "party_id")
	 //@ManyToOne(fetch = FetchType.EAGER)
	 //private UserLogin userLogin;
	
}

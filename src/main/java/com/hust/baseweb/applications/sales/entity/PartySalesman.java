package com.hust.baseweb.applications.sales.entity;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import lombok.Getter;
import lombok.Setter;

import com.hust.baseweb.entity.Person;


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
	 
}

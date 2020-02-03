package com.hust.baseweb.applications.order.entity;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter

public class PartyCustomer {
	@Id
    @Column(name="party_id")
    private UUID partyId;
	
	@Column(name="customer_name")
	private String customerName;
	
}

package com.hust.baseweb.applications.order.entity;

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
@IdClass(CompositeOrderRoleId.class)
public class OrderRole {
	@Id
	@Column(name="order_id")
	private String orderId;
	
	@Id
	@Column(name="party_id")
	private UUID partyId;
	
	@Id
	@Column(name="role_type_id")
	private String roleTypeId;
	
	
}

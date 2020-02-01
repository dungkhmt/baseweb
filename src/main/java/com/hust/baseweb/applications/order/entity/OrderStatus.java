package com.hust.baseweb.applications.order.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.Setter;
import lombok.Getter;

@Entity
@Getter
@Setter
public class OrderStatus {
	@Id
	@Column(name="order_status_id")
	private String orderStatusId;
	
	@JoinColumn(name="order_id", referencedColumnName="order_id")
	@ManyToOne(fetch = FetchType.EAGER)
	private OrderHeader order;
	
	@JoinColumn(name="status_id", referencedColumnName="status_id")
	@ManyToOne(fetch = FetchType.EAGER)
	private StatusItem status;
	
	
	
}

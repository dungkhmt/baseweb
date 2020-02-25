package com.hust.baseweb.applications.sales.entity;

import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class CustomerSalesman {
	@Id
	@Column(name="customer_salesman_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private UUID customerSalesmanId;
	
	@Column(name="party_customer_id")
	private UUID partyCustomerId;
	
	@Column(name="party_salesman_id")
	private UUID partySalesmanId;
	
	@Column(name="from_date")
	private Date fromDate;
	
	@Column(name="thru_date")
	private Date thruDate;
	
	
}

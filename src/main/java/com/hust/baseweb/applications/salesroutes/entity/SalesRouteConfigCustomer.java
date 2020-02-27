package com.hust.baseweb.applications.salesroutes.entity;

import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.hust.baseweb.applications.customer.entity.PartyCustomer;
import com.hust.baseweb.applications.sales.entity.PartySalesman;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class SalesRouteConfigCustomer {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="sales_route_config_customer_id")
	private UUID salesRouteConfigCustomerId;
	
	@JoinColumn(name="sales_route_config_id", referencedColumnName="sales_route_config_id")
	@ManyToOne(fetch=FetchType.EAGER)
	private SalesRouteConfig salesRouteConfig;
	
	@JoinColumn(name="party_customer_id", referencedColumnName="party_id")
	@ManyToOne(fetch=FetchType.EAGER)
	private PartyCustomer partyCustomer;
	
	@JoinColumn(name="party_salesman_id", referencedColumnName="party_id")
	@ManyToOne(fetch=FetchType.EAGER)
	private PartySalesman partySalesman;
	
	@Column(name="from_date")
	private Date fromDate;
	
	@Column(name="thru_date")
	private Date thruDate;
	
	@Column(name="start_execute_date")
	private String startExecuteDate;
	
}

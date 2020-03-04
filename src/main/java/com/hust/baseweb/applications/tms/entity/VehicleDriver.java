package com.hust.baseweb.applications.tms.entity;

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

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class VehicleDriver {
	@Id
	@Column(name="vehicle_driver_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private UUID vehicleDriverId;
	
	@JoinColumn(name="vehicle_id", referencedColumnName="vehicle_id")
	@ManyToOne(fetch=FetchType.EAGER)
	private Vehicle vehicle;
	
	@JoinColumn(name="party_driver_id", referencedColumnName="party_id")
	@ManyToOne(fetch=FetchType.EAGER)
	private PartyDriver partyDriver;
	
	@Column(name="from_date")
	private Date fromDate;
	
	@Column(name="thru_date")
	private Date thruDate;
	
}

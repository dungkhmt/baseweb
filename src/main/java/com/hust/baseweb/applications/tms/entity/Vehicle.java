package com.hust.baseweb.applications.tms.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Vehicle {
	@Id
	@Column(name="vehicle_id")
	private String vehicleId;
	
}

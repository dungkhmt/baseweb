package com.hust.baseweb.applications.tms.entity;

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
public class VehicleType {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="vehicle_type_id")
	private String vehicleTypeId;
	
}

package com.hust.baseweb.applications.geo.entity;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter

public class PostalAddress {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="contact_mech_id")
	private UUID contact_mech_id;
	
	@Column(name="address")
	private String address;
	
	@JoinColumn(name="geo_point_id", referencedColumnName ="geo_point_id")
	@ManyToOne
	private GeoPoint geoPoint;
	
	
}

package com.hust.baseweb.applications.geo.entity;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Getter;
import lombok.Setter;


@Entity
@Setter
@Getter
public class GeoPoint {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="geo_point_id")
	private UUID geoPointId;
	
	@Column(name="latitude")
	private String latitude;
	
	@Column(name="longitude")
	private String longitude;
	
}

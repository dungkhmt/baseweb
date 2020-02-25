package com.hust.baseweb.applications.logistics.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Uom {
	@Id
	@Column(name="uom_id")
	private String uomId;
	
	@Column(name="abbreviation")
	private String abbreviation;
	
	@Column(name="description")
	private String description;
	
}

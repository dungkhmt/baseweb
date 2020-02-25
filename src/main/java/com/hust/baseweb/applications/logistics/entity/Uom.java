package com.hust.baseweb.applications.logistics.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.Fetch;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Uom {
	@Id
	@Column(name="uom_id")
	private String uomId;
	
	@JoinColumn(name="uom_type_id", referencedColumnName="uom_type_id")
	@ManyToOne(fetch=FetchType.EAGER)
	private UomType uomType;
	
	@Column(name="abbreviation")
	private String abbreviation;
	
	@Column(name="description")
	private String description;
	
}

package com.hust.baseweb.applications.tms.entity;

import java.util.List;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
public class Shipment {
	@Id
	@Column(name="shipment_id")
	private UUID shipmentId;
	
	@Column(name="shipment_type_id")
	private String shipmentTypeId;
	
	@JoinColumn(name="shipment_id", referencedColumnName="shipment_id")
	@OneToMany(fetch=FetchType.LAZY)
	private List<ShipmentItem> shipmentItems;
	
	
}

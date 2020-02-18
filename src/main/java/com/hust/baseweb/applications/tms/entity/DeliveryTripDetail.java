package com.hust.baseweb.applications.tms.entity;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter

public class DeliveryTripDetail {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="delivery_trip_detail_id")
	private UUID deliveryTripDetailId;
	
	@Column(name="delivery_trip_id")
	private UUID deliveryTripId;

	
	@JoinColumn(name="shipment_id", referencedColumnName="shipment_id")
	@JoinColumn(name="shipment_item_seq_id", referencedColumnName="shipment_item_seq_id")
	@ManyToOne(fetch=FetchType.EAGER)
	private ShipmentItem shipmentItem;
	
	@Column(name="delivery_quantity")
	private int deliveryQuantity;
	
	
}

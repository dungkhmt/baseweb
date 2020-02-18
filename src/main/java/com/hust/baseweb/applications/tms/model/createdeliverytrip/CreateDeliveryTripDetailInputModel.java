package com.hust.baseweb.applications.tms.model.createdeliverytrip;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Id;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class CreateDeliveryTripDetailInputModel {
	private UUID deliveryTripId;
	private UUID shipmentId;
	private String shipmentItemSeqId;
	private int deliveryQuantity;
	
	
	
}

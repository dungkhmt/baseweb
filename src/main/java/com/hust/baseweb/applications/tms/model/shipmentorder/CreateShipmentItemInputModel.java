package com.hust.baseweb.applications.tms.model.shipmentorder;

import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateShipmentItemInputModel {
	private String orderId;
	private String orderItemSeqId;
	private String productId;
	private int quantity;
	private double amountPallet;
	private UUID customerId;
	private String shipToAddress;
	private String shipToLocation;// lat,lng format
}

package com.hust.baseweb.applications.tms.model.deliverytrip;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class DeliveryTripLocationItemView {
	private UUID deliveryTripDetailId;
	private UUID shipmentItemId;
	private String productId;
	private String productName;
	private int deliveryQuantity;
	
}

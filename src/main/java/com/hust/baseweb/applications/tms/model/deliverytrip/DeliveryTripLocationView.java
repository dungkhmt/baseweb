package com.hust.baseweb.applications.tms.model.deliverytrip;

import java.util.List;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Setter;
import lombok.Getter;

@Getter
@Setter
@AllArgsConstructor
public class DeliveryTripLocationView {
	private String customerName;
	private String address;
	private double latitude;
	private double longitude;
	private UUID partyCustomerId;
	private List<DeliveryTripLocationItemView> items;
}

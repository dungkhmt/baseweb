package com.hust.baseweb.applications.tms.model.deliverytrip;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class DeliveryTripHeaderView {
	private UUID deliveryTripId;
	private String vehicleId;
	private UUID driverPartyId;
	private String driverUserLoginId;
	private Date executeDate;
	private List<DeliveryTripLocationView> deliveryTripLocations;
}

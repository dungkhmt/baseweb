package com.hust.baseweb.applications.tms.model.createdeliverytrip;

import java.util.Date;
import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class CreateDeliveryTripInputModel {
	private UUID deliveryPlanId;
	private String executeDate;
	private String vehicleId;
	private String driverId;
	
	
}

package com.hust.baseweb.applications.salesroutes.model.salesmancheckinout;

import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SalesmanCheckInOutInputModel {
	private UUID partyCustomerId;
	private double latitude;
	private double longitude;
}

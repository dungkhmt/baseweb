package com.hust.baseweb.applications.tracklocations.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetUserLocationsInputModel {
	private double lat1;
	private double lng1;
	private double lat2;
	private double lng2;
	public GetUserLocationsInputModel() {
		super();
		// TODO Auto-generated constructor stub
	}
	
}

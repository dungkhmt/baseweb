package com.hust.baseweb.applications.tracklocations.model;

import java.sql.Timestamp;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class PostLocationInputModel {
	private double lat;
	private double lng;
	private Date timePoint;
	public PostLocationInputModel() {
		super();
		// TODO Auto-generated constructor stub
	}
	
}

package com.hust.baseweb.applications.tms.model.driver;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class DriverModelOutputModel {
	private String userLoginId;
	private String driverFullName;
	private UUID partyDriverId;
	
}

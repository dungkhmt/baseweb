package com.hust.baseweb.applications.tms.model.statistic.vehicledistance;

import lombok.Setter;
import lombok.Getter;

@Getter
@Setter
public class StatisticVehicleDistanceOutputModel {
	private String[] vehicleId;
	private double[] distance;
}

package com.hust.baseweb.applications.salesroutes.model.salesroutedetail;

import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GenerateSalesRouteDetailInputModel {
	private UUID partySalesmanId;
	private UUID salesRoutePlanningPeriodId;
	
}

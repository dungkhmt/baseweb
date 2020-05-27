package com.hust.baseweb.applications.salesroutes.model.salesroutedetail;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class GenerateSalesRouteDetailInputModel {

    private UUID partySalesmanId;
    private UUID salesRoutePlanningPeriodId;

}

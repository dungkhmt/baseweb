package com.hust.baseweb.applications.salesroutes.model.salesrouteplanningperiod;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateSalesRoutePlanningPeriodInputModel {

    private String fromDate;// format YYYY-MM-DD
    private String toDate;// format YYYY-MM-DD
    private String description;
}

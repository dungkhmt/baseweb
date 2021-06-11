package com.hust.baseweb.applications.salesroutes.model.salesrouteconfig;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateSalesRouteConfigInputModel {

    private String visitFrequencyId;
    private String days;// list of days in a week, i.e., 2,5  or 4,7
}

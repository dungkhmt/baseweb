package com.hust.baseweb.applications.salesroutes.model.salesrouteconfig;

import java.util.UUID;

public interface GetListSalesRouteConfigOM {

    UUID getSalesRouteConfigId();

    String getVisitFrequencyId();

    String getDays();

    String getDescription();

    Integer getRepeatWeek();
}

package com.hust.baseweb.applications.salesroutes.model.salesrouteconfigretailoutlets;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class UpdateSalesRouteConfigRetailOutletsIM {

    UUID salesRouteConfigId;
    UUID salesRouteConfigRetailOutletId;
    Integer startExecuteWeek;
    String visitFrequencyId;
}

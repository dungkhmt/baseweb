package com.hust.baseweb.applications.salesroutes.model.salesrouteconfigcustomer;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class CreateSalesRouteConfigRetailOutletIM {

    private UUID salesRouteConfigId;
    private UUID retailOutletSalesmanVendorId;
    private UUID salesRoutePlanningPeriodId;
    private String visitFrequencyId;
    private Integer startExecuteWeek;
    private String startExecuteDate;// format YYYY-MM-DD

}

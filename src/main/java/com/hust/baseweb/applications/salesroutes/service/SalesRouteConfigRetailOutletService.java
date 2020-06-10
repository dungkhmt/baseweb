package com.hust.baseweb.applications.salesroutes.service;

import com.hust.baseweb.applications.salesroutes.entity.SalesRouteConfigRetailOutlet;
import com.hust.baseweb.applications.salesroutes.model.salesrouteconfigcustomer.GetSalesRouteConfigRetailOutletsOM;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface SalesRouteConfigRetailOutletService {

    SalesRouteConfigRetailOutlet save(
        UUID retailOutletSalesmanVendorId,
        String visitFrequencyId,
        UUID salesRouteConfigId,
        UUID salesRoutePlanningPeriodId,
        String startExecuteDate
    );

    List<GetSalesRouteConfigRetailOutletsOM> getSalesRoutesConfigRetailOutlets(UUID salesRoutePlanningPeriodId);

}

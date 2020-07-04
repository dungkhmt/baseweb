package com.hust.baseweb.applications.salesroutes.service;

import com.hust.baseweb.applications.salesroutes.entity.SalesRouteConfigRetailOutlet;
import com.hust.baseweb.applications.salesroutes.model.salesrouteconfigcustomer.CreateSalesRouteConfigRetailOutletIM;
import com.hust.baseweb.applications.salesroutes.model.salesrouteconfigcustomer.GetSalesRouteConfigRetailOutletsOM;
import com.hust.baseweb.applications.salesroutes.model.salesrouteconfigretailoutlets.UpdateSalesRouteConfigRetailOutletsIM;
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
        Integer startExecuteWeek,
        String startExecuteDate
    );

    /**
     * @author AnhTuan-AiT (anhtuan0126104@gmail.com)
     */
    List<GetSalesRouteConfigRetailOutletsOM> getSalesRoutesConfigRetailOutlets(UUID salesRoutePlanningPeriodId);

    /**
     * @author AnhTuan-AiT (anhtuan0126104@gmail.com)
     */
    void updateSalesRoutesConfigRetailOutlet(UpdateSalesRouteConfigRetailOutletsIM input);

    /**
     * @author AnhTuan-AiT (anhtuan0126104@gmail.com)
     */
    int createSalesRouteConfigRetailOutlet(CreateSalesRouteConfigRetailOutletIM input);
}

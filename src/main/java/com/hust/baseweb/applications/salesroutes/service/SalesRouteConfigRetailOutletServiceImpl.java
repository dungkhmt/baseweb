package com.hust.baseweb.applications.salesroutes.service;

import com.hust.baseweb.applications.customer.repo.RetailOutletPagingRepo;
import com.hust.baseweb.applications.sales.entity.RetailOutletSalesmanVendor;
import com.hust.baseweb.applications.sales.repo.PartySalesmanRepo;
import com.hust.baseweb.applications.sales.repo.RetailOutletSalesmanVendorRepo;
import com.hust.baseweb.applications.salesroutes.entity.SalesRouteConfig;
import com.hust.baseweb.applications.salesroutes.entity.SalesRouteConfigRetailOutlet;
import com.hust.baseweb.applications.salesroutes.entity.SalesRoutePlanningPeriod;
import com.hust.baseweb.applications.salesroutes.entity.SalesRouteVisitFrequency;
import com.hust.baseweb.applications.salesroutes.repo.*;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class SalesRouteConfigRetailOutletServiceImpl implements
    SalesRouteConfigRetailOutletService {

    private PSalesRouteConfigRetailOutletRepo pSalesRouteConfigRetailOutletRepo;
    private RetailOutletPagingRepo partyRetailOutletRepo;
    private PartySalesmanRepo partySalesmanRepo;
    private PSalesRouteConfigRepo pSalesRouteConfigRepo;
    private PSalesRoutePlanningPeriodRepo salesRoutePlanningPeriodRepo;
    private RetailOutletSalesmanVendorRepo retailOutletSalesmanVendorRepo;
    private SalesRouteVisitFrequencyRepo salesRouteVisitFrequencyRepo;
    private SalesRouteConfigRetailOutletRepo salesRouteConfigRetailOutletRepo;

    @Override
    public SalesRouteConfigRetailOutlet save(
        UUID retailOutletSalesmanVendorId,
        String visitFrequencyId,
        UUID salesRouteConfigId,
        UUID salesRoutePlanningPeriodId,
        String startExecuteDate) {

        SalesRouteConfigRetailOutlet salesRouteConfigRetailOutlet = new SalesRouteConfigRetailOutlet();
        SalesRouteConfig salesRouteConfig = pSalesRouteConfigRepo.findBySalesRouteConfigId(salesRouteConfigId);
        SalesRoutePlanningPeriod salesRoutePlanningPeriod = salesRoutePlanningPeriodRepo.findBySalesRoutePlanningPeriodId(
            salesRoutePlanningPeriodId);
        RetailOutletSalesmanVendor retailOutletSalesmanVendor = retailOutletSalesmanVendorRepo.findByRetailOutletSalesmanVendorId(
            retailOutletSalesmanVendorId);
        SalesRouteVisitFrequency salesRouteVisitFrequency = salesRouteVisitFrequencyRepo.findByVisitFrequencyId(
            visitFrequencyId);

        salesRouteConfigRetailOutlet.setSalesRouteVisitFrequency(salesRouteVisitFrequency);
        salesRouteConfigRetailOutlet.setSalesRoutePlanningPeriod(salesRoutePlanningPeriod);
        salesRouteConfigRetailOutlet.setRetailOutletSalesmanVendor(retailOutletSalesmanVendor);
        salesRouteConfigRetailOutlet.setSalesRouteConfig(salesRouteConfig);
        salesRouteConfigRetailOutlet.setStartExecuteDate(startExecuteDate);

        salesRouteConfigRetailOutlet = pSalesRouteConfigRetailOutletRepo.save(salesRouteConfigRetailOutlet);

        return salesRouteConfigRetailOutlet;
    }

    @Override
    public List<SalesRouteConfigRetailOutletRepo.GetSalesRouteConfigRetailOutletsOutputModel> getSalesroutesConfigRetailOutlets(
        UUID salesRoutePlanningPeriodId) {

        return salesRouteConfigRetailOutletRepo.getSalesroutesConfigRetailOutlets(salesRoutePlanningPeriodId);
    }
}

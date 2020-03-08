package com.hust.baseweb.applications.salesroutes.service;

import com.hust.baseweb.applications.customer.entity.PartyCustomer;
import com.hust.baseweb.applications.order.repo.PartyCustomerRepo;
import com.hust.baseweb.applications.sales.entity.PartySalesman;
import com.hust.baseweb.applications.sales.repo.PartySalesmanRepo;
import com.hust.baseweb.applications.salesroutes.entity.SalesRouteConfig;
import com.hust.baseweb.applications.salesroutes.entity.SalesRouteConfigCustomer;
import com.hust.baseweb.applications.salesroutes.repo.PSalesRouteConfigCustomerRepo;
import com.hust.baseweb.applications.salesroutes.repo.PSalesRouteConfigRepo;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class SalesRouteConfigCustomerServiceImpl implements
        SalesRouteConfigCustomerService {

    private PSalesRouteConfigCustomerRepo pSalesRouteConfigCustomerRepo;
    private PartyCustomerRepo partyCustomerRepo;
    private PartySalesmanRepo partySalesmanRepo;
    private PSalesRouteConfigRepo pSalesRouteConfigRepo;

    @Override
    public SalesRouteConfigCustomer save(UUID salesRouteConfigId,
                                         UUID partyCustomerId, UUID partySalesmanId, String startExecuteDate) {
        SalesRouteConfigCustomer salesRouteConfigCustomer = new SalesRouteConfigCustomer();
        PartyCustomer partyCustomer = partyCustomerRepo.findByPartyId(partyCustomerId);
        PartySalesman partySalesman = partySalesmanRepo.findByPartyId(partySalesmanId);
        SalesRouteConfig salesRouteConfig = pSalesRouteConfigRepo.findBySalesRouteConfigId(salesRouteConfigId);

        salesRouteConfigCustomer.setFromDate(new Date());
        salesRouteConfigCustomer.setPartyCustomer(partyCustomer);
        salesRouteConfigCustomer.setPartySalesman(partySalesman);
        salesRouteConfigCustomer.setSalesRouteConfig(salesRouteConfig);
        salesRouteConfigCustomer.setStartExecuteDate(startExecuteDate);
        salesRouteConfigCustomer = pSalesRouteConfigCustomerRepo.save(salesRouteConfigCustomer);

        return salesRouteConfigCustomer;
    }

}

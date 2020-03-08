package com.hust.baseweb.applications.salesroutes.service;

import com.hust.baseweb.applications.customer.entity.PartyCustomer;
import com.hust.baseweb.applications.order.repo.PartyCustomerRepo;
import com.hust.baseweb.applications.sales.entity.PartySalesman;
import com.hust.baseweb.applications.sales.repo.PartySalesmanRepo;
import com.hust.baseweb.applications.salesroutes.entity.SalesRouteConfig;
import com.hust.baseweb.applications.salesroutes.entity.SalesRouteConfigCustomer;
import com.hust.baseweb.applications.salesroutes.repo.PSalesRouteConfigCustomerRepo;
import com.hust.baseweb.applications.salesroutes.repo.PSalesRouteConfigRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Service
public class SalesRouteConfigCustomerServiceImpl implements
        SalesRouteConfigCustomerService {

    @Autowired
    private PSalesRouteConfigCustomerRepo pSalesRouteConfigCustomerRepo;

    @Autowired
    private PartyCustomerRepo partyCustomerRepo;

    @Autowired
    private PartySalesmanRepo partySalesmanRepo;

    @Autowired
    private PSalesRouteConfigRepo pSalesRouteConfigRepo;

    @Override
    public SalesRouteConfigCustomer save(UUID salesRouteConfigId,
                                         UUID partyCustomerId, UUID partySalesmanId, String startExecuteDate) {
        // TODO Auto-generated method stub
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

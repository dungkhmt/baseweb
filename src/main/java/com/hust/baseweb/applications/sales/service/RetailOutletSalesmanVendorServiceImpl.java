package com.hust.baseweb.applications.sales.service;

import com.hust.baseweb.applications.customer.entity.PartyDistributor;
import com.hust.baseweb.applications.customer.entity.PartyRetailOutlet;
import com.hust.baseweb.applications.customer.repo.PartyRetailOutletRepo;
import com.hust.baseweb.applications.customer.repo.RetailOutletPagingRepo;
import com.hust.baseweb.applications.order.repo.PartyDistributorRepo;
import com.hust.baseweb.applications.sales.entity.PartySalesman;
import com.hust.baseweb.applications.sales.entity.RetailOutletSalesmanVendor;
import com.hust.baseweb.applications.sales.model.retailoutletsalesmandistributor.RetailOutletSalesmanDistributorInputModel;
import com.hust.baseweb.applications.sales.repo.PartySalesmanRepo;
import com.hust.baseweb.applications.sales.repo.RetailOutletSalesmanVendorRepo;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Log4j2
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class RetailOutletSalesmanVendorServiceImpl implements RetailOutletSalesmanVendorService {

    private RetailOutletSalesmanVendorRepo retailOutletSalesmanVendorRepo;
    private PartySalesmanRepo partySalesmanRepo;
    private PartyDistributorRepo partyDistributorRepo;
    private RetailOutletPagingRepo retailOutletRepo;
    private PartyRetailOutletRepo partyRetailOutletRepo;

    @Override
    public RetailOutletSalesmanVendor save(RetailOutletSalesmanDistributorInputModel input) {
        PartySalesman salesman = partySalesmanRepo.findByPartyId(input.getPartySalesmanId());
        PartyDistributor distributor = partyDistributorRepo.findByPartyId(input.getPartyDistributorId());
        PartyRetailOutlet retailOutlet = retailOutletRepo.findByPartyId(input.getPartyRetailOutletId());

        RetailOutletSalesmanVendor retailOutletSalesmanVendor = new RetailOutletSalesmanVendor();
        retailOutletSalesmanVendor.setPartySalesman(salesman);
        retailOutletSalesmanVendor.setPartyDistributor(distributor);
        retailOutletSalesmanVendor.setPartyRetailOutlet(retailOutlet);
        retailOutletSalesmanVendor.setFromDate(new Date());

        retailOutletSalesmanVendor = retailOutletSalesmanVendorRepo.save(retailOutletSalesmanVendor);

        return retailOutletSalesmanVendor;
    }

    @Override
    public List<RetailOutletSalesmanVendorRepo.GetRetailOutletsOfSalesmanAndDistributor>
        getRetailOutletsOfSalesmanAndDistributor(UUID partySalesmanId, UUID partyDistributorId) {
        /*PartySalesman partySalesman = partySalesmanRepo.findByPartyId(partySalesmanId);
        PartyDistributor partyDistributor = partyDistributorRepo.findByPartyId(partyDistributorId);
        List<RetailOutletSalesmanVendor> list = retailOutletSalesmanVendorRepo.findAllByPartySalesmanAndPartyDistributorAndThruDate(
            partySalesman,
            partyDistributor,
            null);
        List<PartyRetailOutlet> retailOutlets = list
            .stream()
            .map(i -> i.getPartyRetailOutlet())
            .collect(Collectors.toList());*/

        return retailOutletSalesmanVendorRepo.getRetailOutletsOfSalesmanAndDistributor(partySalesmanId, partyDistributorId);
    }

    @Override
    public RetailOutletSalesmanVendor getRetailOutletSalesmanDistributor(
        UUID partyRetailOutletId,
        UUID partySalesmanId,
        UUID partyDistributorId
    ) {
        PartySalesman partySalesman = partySalesmanRepo.findByPartyId(partySalesmanId);
        PartyRetailOutlet partyRetailOutlet = partyRetailOutletRepo.findByPartyId(partyRetailOutletId);
        PartyDistributor partyDistributor = partyDistributorRepo.findByPartyId(partyDistributorId);
        List<RetailOutletSalesmanVendor> retailOutletSalesmanVendors = retailOutletSalesmanVendorRepo.findAllByPartySalesmanAndPartyRetailOutletAndPartyDistributorAndThruDate(
            partySalesman,
            partyRetailOutlet,
            partyDistributor,
            null);
        if (retailOutletSalesmanVendors != null && retailOutletSalesmanVendors.size() > 0) {
            return retailOutletSalesmanVendors.get(0);
        }
        return null;
    }
}

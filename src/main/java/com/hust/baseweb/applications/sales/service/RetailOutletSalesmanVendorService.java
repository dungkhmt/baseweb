package com.hust.baseweb.applications.sales.service;

import com.hust.baseweb.applications.customer.entity.PartyRetailOutlet;
import com.hust.baseweb.applications.sales.entity.RetailOutletSalesmanVendor;
import com.hust.baseweb.applications.sales.model.retailoutletsalesmandistributor.RetailOutletSalesmanDistributorInputModel;
import com.hust.baseweb.applications.sales.repo.RetailOutletSalesmanVendorRepo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface RetailOutletSalesmanVendorService {

    RetailOutletSalesmanVendor save(RetailOutletSalesmanDistributorInputModel input);

    List<RetailOutletSalesmanVendorRepo.GetRetailOutletsOfSalesmanAndDistributor>
        getRetailOutletsOfSalesmanAndDistributor(UUID partySalesmanId, UUID partyDistributorId);

    RetailOutletSalesmanVendor getRetailOutletSalesmanDistributor(
        UUID partyRetailOutletId,
        UUID partySalesmanId,
        UUID partyDistributorId
    );
}

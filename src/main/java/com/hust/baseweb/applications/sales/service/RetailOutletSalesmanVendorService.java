package com.hust.baseweb.applications.sales.service;

import com.hust.baseweb.applications.customer.entity.PartyDistributor;
import com.hust.baseweb.applications.customer.entity.PartyRetailOutlet;
import com.hust.baseweb.applications.sales.entity.RetailOutletSalesmanVendor;
import com.hust.baseweb.applications.sales.model.retailoutletsalesmandistributor.RetailOutletSalesmanDistributorInputModel;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface RetailOutletSalesmanVendorService {
    RetailOutletSalesmanVendor save(RetailOutletSalesmanDistributorInputModel input);
    public List<PartyRetailOutlet> getListRetailOutletOfSalesmanAndDistributor(UUID partySalesmanId, UUID partyDistributorId);
    public RetailOutletSalesmanVendor getRetailOutletSalesmanDistributor(UUID partyRetailOutletId, UUID partySalesmanId, UUID partyDistributorId);
}

package com.hust.baseweb.applications.sales.model.retailoutletsalesmandistributor;

import com.hust.baseweb.applications.sales.entity.RetailOutletSalesmanVendor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class RetailOutletSalesmanDistributorModel {

    private UUID retailOutletSalesmanVendorId;
    private UUID partyRetailOutletId;
    private String retailOutletName;
    private String retailOutletCode;
    private UUID partySalesmanId;
    private String salesmanName;
    private UUID partyDistributorId;
    private String distributorName;
    private String distributorCode;

    public RetailOutletSalesmanDistributorModel(RetailOutletSalesmanVendor retailOutletSalesmanVendor) {

        this.retailOutletSalesmanVendorId = retailOutletSalesmanVendor.getRetailOutletSalesmanVendorId();
        this.partyRetailOutletId = retailOutletSalesmanVendor.getPartyRetailOutlet().getPartyId();
        this.retailOutletName = retailOutletSalesmanVendor.getPartyRetailOutlet().getRetailOutletName();
        this.retailOutletCode = retailOutletSalesmanVendor.getPartyRetailOutlet().getRetailOutletCode();
        this.partySalesmanId = retailOutletSalesmanVendor.getPartySalesman().getPartyId();
        this.salesmanName = retailOutletSalesmanVendor.getPartySalesman().getUserName();
        this.partyDistributorId = retailOutletSalesmanVendor.getPartyDistributor().getPartyId();
        this.distributorCode = retailOutletSalesmanVendor.getPartyDistributor().getDistributorCode();
        this.distributorName = retailOutletSalesmanVendor.getPartyDistributor().getDistributorName();
    }
}

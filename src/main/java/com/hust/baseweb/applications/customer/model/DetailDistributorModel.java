package com.hust.baseweb.applications.customer.model;

import com.hust.baseweb.applications.sales.model.retailoutletsalesmandistributor.RetailOutletSalesmanDistributorModel;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class DetailDistributorModel {

    private UUID partyDistributorId;
    private String distributorName;
    private String distributorCode;
    private List<RetailOutletSalesmanDistributorModel> retailOutletSalesmanDistributorModels;
}

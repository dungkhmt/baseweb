package com.hust.baseweb.applications.sales.model.retailoutletsalesmandistributor;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class RetailOutletSalesmanDistributorInputModel {
    UUID partyRetailOutletId;
    UUID partySalesmanId;
    UUID partyDistributorId;
}

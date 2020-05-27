package com.hust.baseweb.applications.sales.model.retailoutletsalesmandistributor;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GetListRetailOutletsOfSalesmanAndDistributorInputModel {

    private UUID partySalesmanId;
    private UUID partyDistributorId;
}

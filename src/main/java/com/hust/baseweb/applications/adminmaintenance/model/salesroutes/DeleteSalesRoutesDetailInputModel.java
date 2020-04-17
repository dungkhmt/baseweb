package com.hust.baseweb.applications.adminmaintenance.model.salesroutes;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class DeleteSalesRoutesDetailInputModel {
    private UUID partySalesmanId;
    private UUID partyRetailOutletId;
    private UUID partyDistributorId;
}

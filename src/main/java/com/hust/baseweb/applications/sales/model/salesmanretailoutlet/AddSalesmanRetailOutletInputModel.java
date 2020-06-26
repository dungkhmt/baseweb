package com.hust.baseweb.applications.sales.model.salesmanretailoutlet;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class AddSalesmanRetailOutletInputModel {

    private UUID salesmanId;
    private UUID retailOutletId;
}

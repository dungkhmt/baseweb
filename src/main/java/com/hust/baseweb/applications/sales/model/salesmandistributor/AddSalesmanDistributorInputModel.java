package com.hust.baseweb.applications.sales.model.salesmandistributor;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class AddSalesmanDistributorInputModel {

    private UUID salesmanId;
    private UUID distributorId;

}

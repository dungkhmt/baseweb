package com.hust.baseweb.applications.sales.model.salesmandistributor;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GetListDistributorsOfSalesmanInputModel {

    private UUID partySalesmanId;
}

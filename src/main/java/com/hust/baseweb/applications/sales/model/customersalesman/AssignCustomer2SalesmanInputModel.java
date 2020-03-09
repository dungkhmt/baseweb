package com.hust.baseweb.applications.sales.model.customersalesman;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class AssignCustomer2SalesmanInputModel {
    private UUID partyCustomerId;
    private UUID partySalesmanId;

}

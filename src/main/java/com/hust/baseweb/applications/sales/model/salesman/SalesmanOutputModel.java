package com.hust.baseweb.applications.sales.model.salesman;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class SalesmanOutputModel {
    private UUID partyId;
    private String userLoginId;
    private String fullName;
}

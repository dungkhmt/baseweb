package com.hust.baseweb.applications.salesroutes.model.salesmancheckinout;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class SalesmanCheckInOutInputModel {
    private UUID partyCustomerId;
    private double latitude;
    private double longitude;
}

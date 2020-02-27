package com.hust.baseweb.applications.tms.model.createdeliverytrip;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.UUID;

@Getter
@Setter
@ToString
public class CreateDeliveryTripInputModel {
    private UUID deliveryPlanId;
    private String executeDate;
    private String vehicleId;
    private String driverId;


}

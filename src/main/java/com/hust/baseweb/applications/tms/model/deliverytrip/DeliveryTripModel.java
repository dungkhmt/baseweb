package com.hust.baseweb.applications.tms.model.deliverytrip;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DeliveryTripModel {
    private String deliveryPlanSolutionSeqId;
    private String deliveryTripId;
    private String executeDate;
    private Double totalDistance;
    private Double totalWeight;
    private Double totalPallet;
    private String vehicleId;
    private Double maxVehicleCapacity;
    private String driverId;
}

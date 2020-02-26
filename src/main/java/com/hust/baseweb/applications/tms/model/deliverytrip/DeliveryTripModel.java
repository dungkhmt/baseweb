package com.hust.baseweb.applications.tms.model.deliverytrip;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Hien Hoang (hienhoang2702@gmail.com)
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DeliveryTripModel {
    private String deliveryPlanSolutionSeqId;
    private String deliveryTripId;
    private String executeDate;
    private double totalDistance;
    private double totalWeight;
    private String vehicleId;
    private Double maxVehicleCapacity;
}

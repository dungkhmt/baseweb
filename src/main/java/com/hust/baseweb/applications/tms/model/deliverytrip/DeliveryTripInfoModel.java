package com.hust.baseweb.applications.tms.model.deliverytrip;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DeliveryTripInfoModel {
    private String deliveryTripId;
    private Double totalDistance;
    private Double totalWeight;
    private Double totalPallet;
}

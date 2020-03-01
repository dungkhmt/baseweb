package com.hust.baseweb.applications.tms.model.vehicle;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DeleteVehicleDeliveryPlanModel {
    private String deliveryPlanId;
    private String vehicleId;
}

package com.hust.baseweb.applications.tms.model.vehicle;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateVehicleDeliveryPlanModel {
    private String deliveryPlanId;
    private List<String> vehicleIds;
}

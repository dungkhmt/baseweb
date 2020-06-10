package com.hust.baseweb.applications.tms.entity;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class CompositeVehicleDeliveryPlanId implements Serializable {

    private String vehicleId;
    private String deliveryPlanId;
}

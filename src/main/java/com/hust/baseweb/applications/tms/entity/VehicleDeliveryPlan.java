package com.hust.baseweb.applications.tms.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;

@Entity
@Getter
@Setter
@IdClass(CompositeVehicleDeliveryPlanId.class)
@AllArgsConstructor
@NoArgsConstructor
public class VehicleDeliveryPlan {

    @Id
    @Column(name = "vehicle_id")
    private String vehicleId;

    @Id
    @Column(name = "delivery_plan_id")
    private String deliveryPlanId;
}

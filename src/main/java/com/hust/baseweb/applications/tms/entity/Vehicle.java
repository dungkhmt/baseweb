package com.hust.baseweb.applications.tms.entity;

import com.hust.baseweb.applications.tms.model.vehicle.VehicleModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Vehicle {
    @Id
    @Column(name = "vehicle_id")
    private String vehicleId;

    public VehicleModel toVehicleModel() {
        return new VehicleModel(vehicleId);
    }
}

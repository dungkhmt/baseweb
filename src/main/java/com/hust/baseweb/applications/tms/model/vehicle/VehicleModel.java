package com.hust.baseweb.applications.tms.model.vehicle;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class VehicleModel {
    private String vehicleId;
    private Double capacity;
    private Integer length;
    private Integer width;
    private Integer height;
    private Double pallet;
    private String statusId;
    private String description;
}

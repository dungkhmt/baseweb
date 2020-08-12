package com.hust.baseweb.applications.gismap.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class InitBuildStreetInputModel {
    private String streetName;
    private boolean forCar;
    private boolean forTruck;
    private boolean forMotobike;
    private boolean directional;// one-way
}

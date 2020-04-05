package com.hust.baseweb.applications.geo.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ComputeMissingDistanceInputModel {
    private String distanceSource;
    private int speedTruck = 30;
    private int speedMotobike = 40;
}

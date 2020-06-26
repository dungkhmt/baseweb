package com.hust.baseweb.applications.geo.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ComputeMissingDistanceInputModel {

    private String distanceSource;
    private int speedTruck = 30;
    private int speedMotorbike = 40;
    private int maxElements;
}

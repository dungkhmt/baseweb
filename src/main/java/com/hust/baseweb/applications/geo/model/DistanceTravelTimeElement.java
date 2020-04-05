package com.hust.baseweb.applications.geo.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DistanceTravelTimeElement {
    private String fromId;
    private double fromLat;
    private double fromLng;
    private String toId;
    private double toLat;
    private double toLng;
    private double distance;
    private int travelTimeTruck;
    private int travelTimeMotobike;
}

package com.hust.baseweb.applications.geo.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class InputModelDistanceTraveltimePostalAddress {
    private double distance;
    private double travelTime;
    private double travelTimeTruck;
    private double travelTimeMotobike;
    private String enumId;
    private String fromContactMechId;
    private String toContactMechId;
}

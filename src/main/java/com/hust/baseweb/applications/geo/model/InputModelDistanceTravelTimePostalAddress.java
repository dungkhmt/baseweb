package com.hust.baseweb.applications.geo.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class InputModelDistanceTravelTimePostalAddress {

    private double distance;
    private double travelTime;
    private double travelTimeTruck;
    private double travelTimeMotobike;  // TODO: fix typo --> motorbike in frontend
    private String enumId;
    private String fromContactMechId;
    private String toContactMechId;
}

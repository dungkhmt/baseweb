package com.hust.baseweb.algorithmsapi.stafftaskassignment;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class Distance {

    private String fromLocationID;
    private String toLocationID;
    private int travelTime;

    public String toString() {
        return fromLocationID + "-" + toLocationID + ": " + travelTime;
    }
}

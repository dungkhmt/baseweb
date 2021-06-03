package com.hust.baseweb.applications.gismap.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AddPointStreetInputModel {
    private String streetId;
    private double lat;
    private double lng;
    private String timestamp;
}

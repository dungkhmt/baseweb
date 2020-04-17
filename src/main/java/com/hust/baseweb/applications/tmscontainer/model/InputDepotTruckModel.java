package com.hust.baseweb.applications.tmscontainer.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class InputDepotTruckModel {
    String lat;
    String lng;
    String address;
    String depotTruckId;
    String depotTruckName;
}

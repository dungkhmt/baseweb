package com.hust.baseweb.applications.tmscontainer.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class InputDepotTrailerModel {
    String lat;
    String lng;
    String address;
    String depotTrailerId;
    String depotTrailerName;
}

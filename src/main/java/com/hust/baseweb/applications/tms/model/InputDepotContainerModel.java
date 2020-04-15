package com.hust.baseweb.applications.tms.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class InputDepotContainerModel {
    String lat;
    String lng;
    String address;
    String depotContainerId;
    String depotContainerName;
}

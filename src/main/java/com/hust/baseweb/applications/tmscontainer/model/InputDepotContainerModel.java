package com.hust.baseweb.applications.tmscontainer.model;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class InputDepotContainerModel {
    String lat;
    String lng;
    String address;
    String depotContainerId;
    String depotContainerName;
    String contactMechId;
}

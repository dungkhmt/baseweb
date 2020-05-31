package com.hust.baseweb.applications.tmscontainer.model;

import lombok.*;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class InputContRequestExportFullModel {

    String customerId;
    String facilityId;
    String containerTypeId;
    int numberContainer;
    Date earlyDate;
    Date lateDate;
    String trailer;
    String portId;
}

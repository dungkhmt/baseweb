package com.hust.baseweb.applications.tmscontainer.model;

import java.util.Date;
import lombok.*;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class InputContRequestExportEmptyModel{
    String customerId;
    String facilityId;
    String containerTypeId;
    int numberContainer;
    Date earlyDate;
    Date lateDate;
    String trailer;

}

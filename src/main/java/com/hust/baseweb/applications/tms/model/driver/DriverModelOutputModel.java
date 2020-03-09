package com.hust.baseweb.applications.tms.model.driver;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class DriverModelOutputModel {
    private String userLoginId;
    private String driverFullName;
    private UUID partyDriverId;

}

package com.hust.baseweb.applications.humanresource.model;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class AddParty2DepartmentInputModel {
    private UUID partyId;
    private String departmentId;
    private String roleTypeId;
}

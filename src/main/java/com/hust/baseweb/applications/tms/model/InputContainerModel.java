package com.hust.baseweb.applications.tms.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class InputContainerModel {
    private String containerType;
    private String containerId;
    private String containerName;
}

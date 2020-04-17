package com.hust.baseweb.applications.tmscontainer.model;

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

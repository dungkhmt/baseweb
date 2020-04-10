package com.hust.baseweb.algorithmsapi.model.linearprogramming;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LPInputModel {
    private LPVariable[] variables;
    private LPObjective obj;
    private LPConstraint[] constraints;
}

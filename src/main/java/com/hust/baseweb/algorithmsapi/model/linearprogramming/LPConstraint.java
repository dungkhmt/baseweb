package com.hust.baseweb.algorithmsapi.model.linearprogramming;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LPConstraint {
    private double upper;
    private double lower;
    private LPCoefficient[] coefficients;
}

package com.hust.baseweb.applications.tms.service;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.IOException;

public interface SolverService {
    boolean solve(SolverOption solverOption) throws IOException;

    @AllArgsConstructor
    @Getter
    @Setter
    @NoArgsConstructor
    class SolverOption {
        private String deliveryPlanId;
        private Integer timeLimit; // second
    }
}

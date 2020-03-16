package com.hust.baseweb.applications.tms.service;

import java.io.IOException;

public interface SolverService {
    boolean solve(String deliveryPlanId) throws IOException;
}

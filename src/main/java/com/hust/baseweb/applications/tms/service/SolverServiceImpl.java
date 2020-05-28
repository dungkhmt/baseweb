package com.hust.baseweb.applications.tms.service;

import org.springframework.stereotype.Service;

@Service
public class SolverServiceImpl implements SolverService {

    @Override
    public boolean solve(SolverOption solverOption) {
        return false;
    }

    @Override
    public TripSuggestion.Output suggestTrips(TripSuggestion.Input input) {
        return null;
    }
}

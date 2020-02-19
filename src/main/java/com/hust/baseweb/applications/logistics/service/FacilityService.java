package com.hust.baseweb.applications.logistics.service;

import com.hust.baseweb.applications.logistics.entity.Facility;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface FacilityService {
    public Facility findFacilityById(String facilityId);

    public List<Facility> getAllFacilities();
}

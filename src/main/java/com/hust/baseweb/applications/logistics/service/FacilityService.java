package com.hust.baseweb.applications.logistics.service;

import com.hust.baseweb.applications.logistics.entity.Facility;
import com.hust.baseweb.applications.logistics.model.FacilityModel;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface FacilityService {
    Facility findFacilityById(String facilityId);

    List<Facility> getAllFacilities();

    Facility save(FacilityModel facilityModel);
}

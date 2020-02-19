package com.hust.baseweb.applications.logistics.service;

import com.hust.baseweb.applications.logistics.entity.Facility;
import com.hust.baseweb.applications.logistics.repo.FacilityRepo;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class FacilityServiceImpl implements FacilityService {
    private FacilityRepo facilityRepo;

    @Override
    public Facility findFacilityById(String facilityId) {

        return facilityRepo.findByFacilityId(facilityId);
    }

    @Override
    public List<Facility> getAllFacilities() {

        return facilityRepo.findAll();
    }

}

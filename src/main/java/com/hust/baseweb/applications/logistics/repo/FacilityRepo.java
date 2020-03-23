package com.hust.baseweb.applications.logistics.repo;

import com.hust.baseweb.applications.logistics.entity.Facility;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FacilityRepo extends JpaRepository<Facility, String> {
    Facility findByFacilityId(String facilityId);

    List<Facility> findAllByFacilityIdIn(List<String> facilityIds);
}

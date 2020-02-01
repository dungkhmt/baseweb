package com.hust.baseweb.applications.logistics.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hust.baseweb.applications.logistics.entity.Facility;

public interface FacilityRepo extends JpaRepository<Facility, String>{
	Facility findByFacilityId(String facilityId);
}

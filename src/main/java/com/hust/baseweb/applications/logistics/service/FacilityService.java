package com.hust.baseweb.applications.logistics.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hust.baseweb.applications.logistics.entity.Facility;

@Service
public interface FacilityService {
	public Facility findFacilityById(String facilityId);
	public List<Facility> getAllFacilities();
}

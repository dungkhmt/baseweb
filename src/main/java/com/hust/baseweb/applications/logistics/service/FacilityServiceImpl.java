package com.hust.baseweb.applications.logistics.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hust.baseweb.applications.logistics.entity.Facility;
import com.hust.baseweb.applications.logistics.repo.FacilityRepo;

@Service
public class FacilityServiceImpl implements FacilityService {
	@Autowired
	private FacilityRepo facilityRepo;
	
	@Override
	public Facility findFacilityById(String facilityId) {
		// TODO Auto-generated method stub
		return facilityRepo.findByFacilityId(facilityId);
	}

	@Override
	public List<Facility> getAllFacilities() {
		// TODO Auto-generated method stub
		return facilityRepo.findAll();
	}

}

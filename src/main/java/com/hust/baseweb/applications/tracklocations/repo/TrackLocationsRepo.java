package com.hust.baseweb.applications.tracklocations.repo;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.Repository;

import com.hust.baseweb.entity.Application;
import com.hust.baseweb.entity.TrackLocations;

public interface TrackLocationsRepo extends JpaRepository<TrackLocations,UUID>{
	
} 

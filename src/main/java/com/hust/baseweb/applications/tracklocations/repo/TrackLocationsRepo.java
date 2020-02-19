package com.hust.baseweb.applications.tracklocations.repo;

import com.hust.baseweb.applications.tracklocations.entity.TrackLocations;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;


public interface TrackLocationsRepo extends JpaRepository<TrackLocations, UUID> {

} 

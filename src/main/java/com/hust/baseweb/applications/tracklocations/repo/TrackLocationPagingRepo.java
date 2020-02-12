package com.hust.baseweb.applications.tracklocations.repo;

import java.util.UUID;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.hust.baseweb.applications.tracklocations.entity.TrackLocations;

public interface TrackLocationPagingRepo extends
		PagingAndSortingRepository<TrackLocations, UUID> {

}

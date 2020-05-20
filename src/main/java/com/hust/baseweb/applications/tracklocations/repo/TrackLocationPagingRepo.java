package com.hust.baseweb.applications.tracklocations.repo;

import com.hust.baseweb.applications.tracklocations.entity.TrackLocations;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.UUID;

public interface TrackLocationPagingRepo extends
    PagingAndSortingRepository<TrackLocations, UUID> {

}

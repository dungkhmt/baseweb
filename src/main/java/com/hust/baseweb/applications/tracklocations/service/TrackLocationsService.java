package com.hust.baseweb.applications.tracklocations.service;

import com.hust.baseweb.applications.tracklocations.entity.TrackLocations;
import com.hust.baseweb.applications.tracklocations.model.PostLocationInputModel;
import com.hust.baseweb.entity.Party;

import java.util.List;


public interface TrackLocationsService {
    TrackLocations save(PostLocationInputModel input, Party party);

    List<TrackLocations> getListLocations();

}

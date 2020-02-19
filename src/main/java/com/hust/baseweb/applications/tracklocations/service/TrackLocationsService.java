package com.hust.baseweb.applications.tracklocations.service;

import com.hust.baseweb.applications.tracklocations.entity.TrackLocations;
import com.hust.baseweb.applications.tracklocations.model.PostLocationInputModel;
import com.hust.baseweb.entity.Party;

import java.util.List;


public interface TrackLocationsService {
    public TrackLocations save(PostLocationInputModel input, Party party);

    public List<TrackLocations> getListLocations();

}

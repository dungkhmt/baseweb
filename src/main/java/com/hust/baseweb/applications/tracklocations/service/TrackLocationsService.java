package com.hust.baseweb.applications.tracklocations.service;

import java.util.List;

import org.springframework.data.geo.Point;

import com.hust.baseweb.applications.tracklocations.entity.TrackLocations;
import com.hust.baseweb.applications.tracklocations.model.PostLocationInputModel;
import com.hust.baseweb.entity.Party;




public interface TrackLocationsService {
	public TrackLocations save(PostLocationInputModel input, Party party);
	public List<TrackLocations> getListLocations();
	
}

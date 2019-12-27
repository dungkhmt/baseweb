package com.hust.baseweb.service;

import java.util.List;

import com.hust.baseweb.entity.Party;
import com.hust.baseweb.entity.TrackLocations;
import com.hust.baseweb.model.PostLocationInputModel;


public interface TrackLocationsService {
	public TrackLocations save(PostLocationInputModel input, Party party);
	public List<TrackLocations> getListLocations();
	
}

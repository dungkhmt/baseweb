package com.hust.baseweb.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hust.baseweb.entity.Party;
import com.hust.baseweb.entity.TrackLocations;
import com.hust.baseweb.model.PostLocationInputModel;
import com.hust.baseweb.repo.TrackLocationsRepo;

@Service
public class TrackLocationsServiceImpl implements TrackLocationsService {
	@Autowired
	TrackLocationsRepo trackLocationsRepo;
	
	@Override
	public TrackLocations save(PostLocationInputModel input, Party party) {
		// TODO Auto-generated method stub
		TrackLocations o = new TrackLocations();
		o.setParty(party);
		o.setLocation(input.getLat() + "," + input.getLng());
		o.setTimePoint(input.getTimePoint());
		
		
		return trackLocationsRepo.save(o);
	}
	public List<TrackLocations> getListLocations(){
		return trackLocationsRepo.findAll();
	}
}

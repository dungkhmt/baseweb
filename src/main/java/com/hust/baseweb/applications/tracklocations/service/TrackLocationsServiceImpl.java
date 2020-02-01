package com.hust.baseweb.applications.tracklocations.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.core.GeoOperations;
import org.springframework.stereotype.Service;





import com.hust.baseweb.applications.tracklocations.entity.TrackLocations;
import com.hust.baseweb.applications.tracklocations.model.PostLocationInputModel;
import com.hust.baseweb.applications.tracklocations.repo.TrackLocationsRepo;
import com.hust.baseweb.entity.Party;




@Service
public class TrackLocationsServiceImpl implements TrackLocationsService {
	public static final String module = TrackLocationsServiceImpl.class.getName();
	//public static final String GROUP = "GROUP";
	
	//@Resource(name = "redisTemplate")
	//private GeoOperations<String, Object> geoOps;
	//public static GeoPointCache geoPointCache = new GeoPointCache();
	
	@Autowired
	TrackLocationsRepo trackLocationsRepo;
	
	@Override
	public TrackLocations save(PostLocationInputModel input, Party party) {
		// TODO Auto-generated method stub
		TrackLocations o = new TrackLocations();
		o.setParty(party);
		o.setLocation(input.getLat() + "," + input.getLng());
		o.setTimePoint(input.getTimePoint());
		
		//geoOps.add(GROUP, new Point(input.getLat(),input.getLng()),"party.getPartyId().toString()");
		//geoPointCache.put(party.getPartyId().toString(), input.getLat(), input.getLng());
		
		return trackLocationsRepo.save(o);
	}
	public List<TrackLocations> getListLocations(){
		return trackLocationsRepo.findAll();
	}
	
}

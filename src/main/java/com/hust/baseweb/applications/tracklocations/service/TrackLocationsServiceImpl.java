package com.hust.baseweb.applications.tracklocations.service;

import com.hust.baseweb.applications.tracklocations.entity.TrackLocations;
import com.hust.baseweb.applications.tracklocations.model.PostLocationInputModel;
import com.hust.baseweb.applications.tracklocations.repo.TrackLocationsRepo;
import com.hust.baseweb.entity.Party;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class TrackLocationsServiceImpl implements TrackLocationsService {
    public static final String module = TrackLocationsServiceImpl.class.getName();
    //public static final String GROUP = "GROUP";

    //@Resource(name = "redisTemplate")
    //private GeoOperations<String, Object> geoOps;
    //public static GeoPointCache geoPointCache = new GeoPointCache();

    TrackLocationsRepo trackLocationsRepo;

    @Override
    public TrackLocations save(PostLocationInputModel input, Party party) {

        TrackLocations trackLocation = new TrackLocations();
        //o.setParty(party);
        trackLocation.setPartyId(party.getPartyId());
        trackLocation.setLocation(input.getLat() + "," + input.getLng());
        trackLocation.setTimePoint(input.getTimePoint());

        //geoOps.add(GROUP, new Point(input.getLat(),input.getLng()),"party.getPartyId().toString()");
        //geoPointCache.put(party.getPartyId().toString(), input.getLat(), input.getLng());

        return trackLocationsRepo.save(trackLocation);
    }

    public List<TrackLocations> getListLocations() {
        return trackLocationsRepo.findAll();
    }

}

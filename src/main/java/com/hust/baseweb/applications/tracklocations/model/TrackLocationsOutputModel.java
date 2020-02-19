package com.hust.baseweb.applications.tracklocations.model;

import com.hust.baseweb.applications.tracklocations.entity.TrackLocations;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.UUID;

@Getter
@Setter
public class TrackLocationsOutputModel {
    private UUID trackLocationId;

    private String partyId;

    private String location;

    private Date timePoint;

    public TrackLocationsOutputModel(TrackLocations tl) {
        this.trackLocationId = tl.getTrackLocationId();
        //this.partyId = tl.getParty().getPartyId().toString();
        this.partyId = tl.getPartyId().toString();
        this.location = tl.getLocation();
        this.timePoint = tl.getTimePoint();
    }
}

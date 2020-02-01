package com.hust.baseweb.applications.tracklocations.model;

import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import com.hust.baseweb.applications.tracklocations.entity.TrackLocations;
import com.hust.baseweb.entity.Party;



import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TrackLocationsOutputModel {
	private UUID trackLocationId;
	
	private String partyId;
	
	private String location;
	
	private Date timePoint;
	
	public TrackLocationsOutputModel(TrackLocations tl){
		this.trackLocationId = tl.getTrackLocationId();
		this.partyId = tl.getParty().getPartyId().toString();
		this.location = tl.getLocation();
		this.timePoint = tl.getTimePoint();
	}
}

package com.hust.baseweb.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

import java.sql.Timestamp;
import java.util.Date;
import java.util.UUID;

@Entity
@Getter
@Setter
public class TrackLocations {
	@Id
	@Column(name="track_location_id")
	private UUID trackLocationId;
	
	
	@JoinColumn(name = "party_id", referencedColumnName = "party_id")
    @OneToOne(fetch = FetchType.EAGER)
    private Party party;
	
	@Column(name="location")
	private String location;
	
	@Column(name="time_point")
	private Date timePoint;
	
	
	private Date createdStamp;
    private Date lastUpdatedStamp;
	public TrackLocations() {
		super();
		// TODO Auto-generated constructor stub
	}
    
    
}

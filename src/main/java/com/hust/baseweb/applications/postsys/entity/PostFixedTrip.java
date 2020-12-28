package com.hust.baseweb.applications.postsys.entity;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name="post_office_fixed_trip")
@Getter
@Setter
public class PostFixedTrip {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="post_office_fixed_trip_id")
    private UUID postOfficeFixedTripId;

    @OneToOne
    @JoinColumn(name="post_office_trip_id", referencedColumnName = "post_office_trip_id", insertable = false, updatable = false)
    private PostOfficeTrip postOfficeTrip;

    @Column(name="post_office_trip_id")
    private UUID postOfficeTripId;

    @Column(name="schedule_departure_time")
    private String scheduleDepartureTime;
    @Column(name="from_date")
    private Date FromDate;
    @Column(name="thru_date")
    private Date ThruDate;

}

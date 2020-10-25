package com.hust.baseweb.applications.postsys.entity;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Date;
import java.util.UUID;

@Entity
@Table(name="post_office_fixed_trip")
@Getter
@Setter
public class PostTrip {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="post_office_fixed_trip_id")
    private UUID postOfficeFixedTripId;

    @ManyToOne
    @JoinColumn(name="from_post_office_id", referencedColumnName = "post_office_id")
    private PostOffice fromPostOffice;

    @ManyToOne
    @JoinColumn(name="to_post_office_id", referencedColumnName = "post_office_id")
    private PostOffice toPostOffice;

    @Column(name="schedule_departure_time")
    private String ScheduleDepartureTime;
    @Column(name="from_date")
    private Date FromDate;
    @Column(name="thru_date")
    private Date ThruDate;
}

package com.hust.baseweb.applications.postsys.entity;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name="post_office_trip")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructorp
public class PostTrip {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="post_office_trip_id")
    private UUID postOfficeTripId;

    @ManyToOne
    @JoinColumn(name="from_post_office_id", referencedColumnName = "post_office_id")
    private PostOffice fromPostOffice;

    @ManyToOne
    @JoinColumn(name="to_post_office_id", referencedColumnName = "post_office_id")
    private PostOffice toPostOffice;

    @Column(name="from_date")
    private Date FromDate;
    @Column(name="thru_date")
    private Date ThruDate;
}

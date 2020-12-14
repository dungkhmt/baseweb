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
@AllArgsConstructor
public class PostOfficeTrip {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="post_office_trip_id")
    private UUID postOfficeTripId;

    @ManyToOne
    @JoinColumn(name="from_post_office_id", referencedColumnName = "post_office_id", insertable = false, updatable = false)
    private PostOffice fromPostOffice;

    @ManyToOne
    @JoinColumn(name="to_post_office_id", referencedColumnName = "post_office_id", insertable = false, updatable = false)
    private PostOffice toPostOffice;

    @Column(name="from_post_office_id")
    private String fromPostOfficeId;

    @Column(name="to_post_office_id")
    private String toPostOfficeId;

    @Column(name="from_date")
    private Date FromDate;
    @Column(name="thru_date")
    private Date ThruDate;
}

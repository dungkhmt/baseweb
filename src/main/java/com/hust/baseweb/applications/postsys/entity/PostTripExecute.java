package com.hust.baseweb.applications.postsys.entity;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "post_office_fixed_trip_execute")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostTripExecute {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "post_office_fixed_trip_execute_id")
    private UUID postOfficeFixedTripExecuteId;
    @Column(name = "post_office_fixed_trip_id")
    private UUID postOfficeFixedTripId;
    @Column(name = "postman_id")
    private UUID postmanId;
    @Column(name = "departure_date_time")
    private Date departureDateTime;
    @Column(name = "arrived_date_time")
    private Date arrivedDateTime;
    @Column(name = "status")
    private String status;
    @Column(name = "created_stamp", insertable = false, updatable = false)
    private Date createdStamp;

    @OneToOne()
    @JoinColumn(name = "postman_id", referencedColumnName = "postman_id", insertable = false, updatable = false)
    Postman postman;

    @OneToOne
    @JoinColumn(name = "post_office_fixed_trip_id",
                referencedColumnName = "post_office_fixed_trip_id",
                insertable = false,
                updatable = false)
    private PostFixedTrip postOfficeFixedTrip;


}

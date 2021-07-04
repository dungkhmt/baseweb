package com.hust.baseweb.applications.postsys.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "post_driver_post_office_assignment")
@Getter
@Setter
public class PostDriverPostOfficeAssignment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "post_driver_post_office_assignment_id")
    private UUID postDriverPostOfficeAssignmentId;
    @Column(name = "post_driver_id")
    private UUID postDriverId;
    @Column(name = "post_office_fixed_trip_id")
    private UUID postOfficeFixedTripId;

    @ManyToOne
    @JoinColumn(name = "post_driver_id", referencedColumnName = "post_driver_id", insertable = false, updatable = false)
    PostDriver postDriver;

    @ManyToOne
    @JoinColumn(name = "post_office_fixed_trip_id",
                referencedColumnName = "post_office_fixed_trip_id",
                updatable = false,
                insertable = false)
    PostFixedTrip postFixedTrip;
}

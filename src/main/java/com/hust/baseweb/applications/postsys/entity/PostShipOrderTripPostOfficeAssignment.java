package com.hust.baseweb.applications.postsys.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name="post_ship_order_trip_post_office_assignment")
@Entity
public class PostShipOrderTripPostOfficeAssignment {

    @Id
    @Column(name="post_ship_order_trip_post_office_assignment_id")
    private UUID postShipOrderPostOfficeTripAssignmentId;
    @Column(name="post_ship_order_id")
    private UUID postShipOrderId;
    @Column(name="post_office_trip_id")
    private UUID postOfficeTripId;
    @Column(name="order")
    private String order;
}

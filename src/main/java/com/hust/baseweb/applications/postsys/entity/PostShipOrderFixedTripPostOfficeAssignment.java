package com.hust.baseweb.applications.postsys.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name="post_ship_order_fixed_trip_post_office_assignment")
@Entity
public class PostShipOrderFixedTripPostOfficeAssignment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="post_ship_order_fixed_trip_post_office_assignment_id")
    private UUID postShipOrderFixedTripPostOfficeAssignmentId;
    @Column(name="post_ship_order_id")
    private UUID postShipOrderId;
    @Column(name="post_office_fixed_trip_execute_id")
    private UUID postOfficeFixedTripExecuteId;
    @Column(name="delivery_order")
    private int delivery_order;
}

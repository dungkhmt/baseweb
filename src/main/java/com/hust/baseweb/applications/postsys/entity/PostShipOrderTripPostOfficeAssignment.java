package com.hust.baseweb.applications.postsys.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "post_ship_order_trip_post_office_assignment")
@Entity
public class PostShipOrderTripPostOfficeAssignment {

    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    @Column(name = "post_ship_order_trip_post_office_assignment_id")
    private UUID postShipOrderPostOfficeTripAssignmentId;
    @Column(name = "post_ship_order_id")
    private UUID postShipOrderId;

    @OneToOne
    @JoinColumn(name = "post_ship_order_id",
                referencedColumnName = "post_ship_order_id",
                insertable = false,
                updatable = false)
    private PostOrder postOrder;

    @Column(name = "post_office_trip_id")
    private UUID postOfficeTripId;

    @OneToOne
    @JoinColumn(name = "post_office_trip_id",
                referencedColumnName = "post_office_trip_id",
                insertable = false,
                updatable = false)
    private PostOfficeTrip postOfficeTrip;

    @Column(name = "delivery_order")
    private int deliveryOrder;

    @Column(name = "created_stamp", updatable = false, insertable = false)
    private Date createdStamp;

    public PostShipOrderTripPostOfficeAssignment(UUID postShipOrderId, PostOrder postOrder, int deliveryOrder) {
        this.postShipOrderId = postShipOrderId;
        this.postOrder = postOrder;
        this.deliveryOrder = deliveryOrder;
    }
}

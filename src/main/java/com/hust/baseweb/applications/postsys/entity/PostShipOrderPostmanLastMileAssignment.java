package com.hust.baseweb.applications.postsys.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name="post_ship_order_postman_last_mile_assignment")
public class PostShipOrderPostmanLastMileAssignment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="post_ship_order_postman_last_mile_assignment_id")
    private UUID postShipOrderPostmanLastMileAssignmentId;
    @Column(name="post_ship_order_id")
    private UUID postShipOrderId;
    @Column(name="postman_id")
    private UUID postmanId;
    @Column(name="pickup_delivery")
    private String pickupDelivery;
    @Column(name="status_id")
    private String statusId;
    @Column(name="created_stamp")
    private Date createdStamp;

    @OneToOne()
    @JoinColumn(name="post_ship_order_id", referencedColumnName = "post_ship_order_id",insertable = false, updatable = false)
    private PostOrder postOrder;
}

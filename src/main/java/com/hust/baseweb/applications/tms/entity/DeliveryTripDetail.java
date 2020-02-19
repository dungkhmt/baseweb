package com.hust.baseweb.applications.tms.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Getter
@Setter

public class DeliveryTripDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "delivery_trip_detail_id")
    private UUID deliveryTripDetailId;

    @Column(name = "delivery_trip_id")
    private UUID deliveryTripId;


    @JoinColumn(name = "shipment_id", referencedColumnName = "shipment_id")
    @JoinColumn(name = "shipment_item_seq_id", referencedColumnName = "shipment_item_seq_id")
    @ManyToOne(fetch = FetchType.EAGER)
    private ShipmentItem shipmentItem;

    @Column(name = "delivery_quantity")
    private int deliveryQuantity;


}

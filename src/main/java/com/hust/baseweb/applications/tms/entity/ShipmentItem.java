package com.hust.baseweb.applications.tms.entity;

import com.hust.baseweb.applications.customer.entity.PartyCustomer;
import com.hust.baseweb.applications.geo.entity.PostalAddress;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Getter
@Setter
@IdClass(CompositeShipmentItemId.class)
public class ShipmentItem {
    @Id
    @Column(name = "shipment_id")
    private UUID shipmentId;

    @Id
    @Column(name = "shipment_item_seq_id")
    private String shipmentItemSeqId;

    @Column(name = "quantity")
    private int quantity;

    @Column(name = "pallet")
    private double pallet;

    @Column(name = "product_id")
    private String productId;

    @JoinColumn(name = "party_customer_id", referencedColumnName = "party_id")
    @ManyToOne
    private PartyCustomer customer;

    @JoinColumn(name = "ship_to_location_id", referencedColumnName = "contact_mech_id")
    @ManyToOne
    private PostalAddress shipToLocation;

}

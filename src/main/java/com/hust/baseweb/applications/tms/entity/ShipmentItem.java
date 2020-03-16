package com.hust.baseweb.applications.tms.entity;

import com.hust.baseweb.applications.customer.entity.PartyCustomer;
import com.hust.baseweb.applications.geo.entity.GeoPoint;
import com.hust.baseweb.applications.geo.entity.PostalAddress;
import com.hust.baseweb.applications.order.entity.OrderItem;
import com.hust.baseweb.applications.tms.model.ShipmentItemModel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Getter
@Setter
//@IdClass(CompositeShipmentItemId.class)
public class ShipmentItem {
    @Id
    @Column(name = "shipment_item_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID shipmentItemId;

    @JoinColumn(name = "shipment_id", referencedColumnName = "shipment_id")
    @ManyToOne
    private Shipment shipment;

    //@Id
    //@Column(name = "shipment_item_seq_id")
    //private String shipmentItemSeqId;

    @Column(name = "quantity")
    private int quantity;

    @Column(name = "pallet")
    private double pallet;

    @JoinColumn(name = "order_id", referencedColumnName = "order_id")
    @JoinColumn(name = "order_item_seq_id", referencedColumnName = "order_item_seq_id")
    @OneToOne(fetch = FetchType.EAGER)
    private OrderItem orderItem;

    @JoinColumn(name = "party_customer_id", referencedColumnName = "party_id")
    @ManyToOne(fetch = FetchType.EAGER)
    private PartyCustomer customer;

    @JoinColumn(name = "ship_to_location_id", referencedColumnName = "contact_mech_id")
    @ManyToOne(fetch = FetchType.EAGER)
    private PostalAddress shipToLocation;

    public ShipmentItemModel toShipmentItemModel() {
        String customerCode = null;
        String locationCode = null;
        String address = null;
        String lat = null;
        String lng = null;
        if (customer != null) {
            customerCode = customer.getCustomerCode();
        }
        if (shipToLocation != null) {
            locationCode = shipToLocation.getLocationCode();
            address = shipToLocation.getAddress();
            if (shipToLocation.getGeoPoint() != null) {
                GeoPoint geoPoint = shipToLocation.getGeoPoint();
                lat = geoPoint.getLatitude();
                lng = geoPoint.getLongitude();
            }
        }

        return new ShipmentItemModel(
                shipmentItemId.toString(),
                quantity,
                pallet,
                orderItem.getProduct().getProductId(),
                customerCode,
                locationCode,
                address,
                lat,
                lng
        );
    }

    public ShipmentItemModel.DeliveryPlan toShipmentItemDeliveryPlanModel(int assignedQuantity) {
        return new ShipmentItemModel.DeliveryPlan(
                shipmentItemId,
                orderItem.getProduct().getProductName(),
                orderItem.getProduct().getWeight(),
                quantity - assignedQuantity,
                pallet,
                shipToLocation.getAddress(),
                shipToLocation.getGeoPoint().getLatitude() + "," +
                        shipToLocation.getGeoPoint().getLongitude()
        );
    }
}

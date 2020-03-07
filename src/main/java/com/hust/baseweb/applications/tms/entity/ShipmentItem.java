package com.hust.baseweb.applications.tms.entity;

import com.hust.baseweb.applications.customer.entity.PartyCustomer;
import com.hust.baseweb.applications.geo.entity.PostalAddress;
import com.hust.baseweb.applications.logistics.entity.Product;
import com.hust.baseweb.applications.tms.model.shipmentitem.ShipmentItemDeliveryPlanModel;
import com.hust.baseweb.applications.tms.model.shipmentitem.ShipmentItemModel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Map;
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

    @Column(name = "product_id")
    private String productId;

    @JoinColumn(name = "party_customer_id", referencedColumnName = "party_id")
    @ManyToOne(fetch = FetchType.EAGER)
    private PartyCustomer customer;

    @JoinColumn(name = "ship_to_location_id", referencedColumnName = "contact_mech_id")
    @ManyToOne(fetch = FetchType.EAGER)
    private PostalAddress shipToLocation;

    public ShipmentItemModel toShipmentItemModel() {
        return new ShipmentItemModel(
                shipmentItemId.toString(),
                quantity,
                pallet,
                productId,
                customer == null ? null : customer.getCustomerCode(),
                shipToLocation == null ? null : shipToLocation.getLocationCode()
        );
    }

    public ShipmentItemDeliveryPlanModel toShipmentItemDeliveryPlanModel(Map<String, Product> productMap, int assignedQuantity) {
        Product product = productMap.get(productId);
        return new ShipmentItemDeliveryPlanModel(
                shipmentItemId,
                product.getProductName(),
                product.getWeight() / quantity,
                quantity - assignedQuantity,
                pallet,
                shipToLocation.getAddress(),
                shipToLocation.getGeoPoint().getLatitude() + "," +
                        shipToLocation.getGeoPoint().getLongitude()
        );
    }
}

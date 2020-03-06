package com.hust.baseweb.applications.tms.entity;

import com.hust.baseweb.applications.logistics.entity.Product;
import com.hust.baseweb.applications.tms.model.deliverytripdetail.DeliveryTripDetailModel;
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


    //@JoinColumn(name = "shipment_id", referencedColumnName = "shipment_id")
    //@JoinColumn(name = "shipment_item_seq_id", referencedColumnName = "shipment_item_seq_id")
    @JoinColumn(name = "shipment_item_id", referencedColumnName = "shipment_item_id")
    @ManyToOne(fetch = FetchType.EAGER)
    private ShipmentItem shipmentItem;

    @Column(name = "delivery_quantity")
    private int deliveryQuantity;

    public DeliveryTripDetailModel toDeliveryTripDetailModel(Product product) {
        return new DeliveryTripDetailModel(
                deliveryTripDetailId,
                deliveryTripId,
                (shipmentItem == null || shipmentItem.getCustomer() == null) ? null : shipmentItem.getCustomer().getCustomerCode(),
                (shipmentItem == null || shipmentItem.getShipToLocation() == null) ? null : shipmentItem.getShipToLocation().getAddress(),
                product == null ? null : product.getProductId(),
                product == null ? null : product.getProductName(),
                shipmentItem == null ? null : shipmentItem.getQuantity(),
                deliveryQuantity,
                product == null ? null : (product.getWeight() / shipmentItem.getQuantity() * deliveryQuantity)
        );
    }

}

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

    private Integer sequence;

    //@JoinColumn(name = "shipment_id", referencedColumnName = "shipment_id")
    //@JoinColumn(name = "shipment_item_seq_id", referencedColumnName = "shipment_item_seq_id")
    @JoinColumn(name = "shipment_item_id", referencedColumnName = "shipment_item_id")
    @ManyToOne(fetch = FetchType.EAGER)
    private ShipmentItem shipmentItem;

    @Column(name = "delivery_quantity")
    private int deliveryQuantity;

    public DeliveryTripDetailModel toDeliveryTripDetailModel(Product product) {
        DeliveryTripDetailModel deliveryTripDetailModel = new DeliveryTripDetailModel();
        deliveryTripDetailModel.setSequence(sequence);
        deliveryTripDetailModel.setDeliveryTripDetailId(deliveryTripDetailId);
        deliveryTripDetailModel.setDeliveryTripId(deliveryTripId);
        if (shipmentItem != null) {
            if (shipmentItem.getCustomer() != null) {
                deliveryTripDetailModel.setCustomerCode(shipmentItem.getCustomer().getCustomerCode());
            }
            if (shipmentItem.getShipToLocation() != null) {
                deliveryTripDetailModel.setAddress(shipmentItem.getShipToLocation().getAddress());
                if (shipmentItem.getShipToLocation().getGeoPoint() != null) {
                    deliveryTripDetailModel.setLat(Double.parseDouble(shipmentItem.getShipToLocation().getGeoPoint().getLatitude()));
                    deliveryTripDetailModel.setLng(Double.parseDouble(shipmentItem.getShipToLocation().getGeoPoint().getLongitude()));
                }
            }
            deliveryTripDetailModel.setShipmentQuantity(shipmentItem.getQuantity());
        }
        if (product != null) {
            deliveryTripDetailModel.setProductId(product.getProductId());
            deliveryTripDetailModel.setProductName(product.getProductName());
            deliveryTripDetailModel.setWeight(product.getWeight() / shipmentItem.getQuantity() * deliveryQuantity);
        }
        deliveryTripDetailModel.setDeliveryQuantity(deliveryQuantity);
        return deliveryTripDetailModel;
    }

}

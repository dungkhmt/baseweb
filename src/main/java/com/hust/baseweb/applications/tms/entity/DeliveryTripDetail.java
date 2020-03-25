package com.hust.baseweb.applications.tms.entity;

import com.hust.baseweb.applications.logistics.entity.Product;
import com.hust.baseweb.applications.tms.model.DeliveryTripDetailModel;
import com.hust.baseweb.entity.StatusItem;
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

    @JoinColumn(name = "status_id", referencedColumnName = "status_id")
    @ManyToOne(fetch = FetchType.EAGER)
    private StatusItem statusItem;

    public DeliveryTripDetailModel.OrderItem toDeliveryTripDetailModel(Product product) {
        DeliveryTripDetailModel.OrderItem orderItemModel = new DeliveryTripDetailModel.OrderItem();
        orderItemModel.setSequence(sequence);
        orderItemModel.setDeliveryTripDetailId(deliveryTripDetailId);
        orderItemModel.setDeliveryTripId(deliveryTripId);
        if (shipmentItem != null) {
            if (shipmentItem.getCustomer() != null) {
                orderItemModel.setCustomerCode(shipmentItem.getCustomer().getCustomerCode());
            }
            if (shipmentItem.getShipToLocation() != null) {
                orderItemModel.setAddress(shipmentItem.getShipToLocation().getAddress());
                if (shipmentItem.getShipToLocation().getGeoPoint() != null) {
                    orderItemModel.setLat(Double.parseDouble(shipmentItem.getShipToLocation()
                            .getGeoPoint()
                            .getLatitude()));
                    orderItemModel.setLng(Double.parseDouble(shipmentItem.getShipToLocation()
                            .getGeoPoint()
                            .getLongitude()));
                }
            }
            orderItemModel.setShipmentQuantity(shipmentItem.getQuantity());
        }
        if (product != null) {
            orderItemModel.setProductId(product.getProductId());
            orderItemModel.setProductName(product.getProductName());
            orderItemModel.setWeight(product.getWeight() * deliveryQuantity);
        }
        orderItemModel.setDeliveryQuantity(deliveryQuantity);
        return orderItemModel;
    }

}

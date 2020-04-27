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

    @JoinColumn(name = "delivery_trip_id", referencedColumnName = "delivery_trip_id")
    @ManyToOne(fetch = FetchType.EAGER)
    private DeliveryTrip deliveryTrip;

    private Integer sequenceId;

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

    public DeliveryTripDetailModel.OrderItem toDeliveryTripDetailModel() {
        DeliveryTripDetailModel.OrderItem orderItemModel = new DeliveryTripDetailModel.OrderItem();
        orderItemModel.setSequence(sequenceId);
        orderItemModel.setDeliveryTripDetailId(deliveryTripDetailId);
        orderItemModel.setDeliveryTripId(deliveryTrip.getDeliveryTripId());
        if (shipmentItem != null) {
            //if (shipmentItem.getCustomer() != null) {
            //    orderItemModel.setCustomerCode(shipmentItem.getCustomer().getCustomerCode());
            //}
            if (shipmentItem.getPartyCustomer() != null) {
                orderItemModel.setCustomerCode(shipmentItem.getPartyCustomer().getPartyCode());
            }

            if (shipmentItem.getShipToLocation() != null) {
                orderItemModel.setAddress(shipmentItem.getShipToLocation().getAddress());
                if (shipmentItem.getShipToLocation().getGeoPoint() != null) {
                    orderItemModel.setLat(shipmentItem.getShipToLocation().getGeoPoint().getLatitude());
                    orderItemModel.setLng(shipmentItem.getShipToLocation().getGeoPoint().getLongitude());
                }
                orderItemModel.setLocationCode(shipmentItem.getShipToLocation().getLocationCode());
            }
            orderItemModel.setShipmentQuantity(shipmentItem.getQuantity());

            Product product = shipmentItem.getOrderItem().getProduct();
            if (product != null) {
                orderItemModel.setProductId(product.getProductId());
                orderItemModel.setProductName(product.getProductName());
                orderItemModel.setWeight(product.getWeight() * deliveryQuantity);
            }
        }
        orderItemModel.setDeliveryQuantity(deliveryQuantity);
        orderItemModel.setStatusId(statusItem.getStatusId());
        return orderItemModel;
    }

}

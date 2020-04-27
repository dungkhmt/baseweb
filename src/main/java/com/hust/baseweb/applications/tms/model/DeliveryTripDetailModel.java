package com.hust.baseweb.applications.tms.model;

import lombok.*;

import java.util.List;
import java.util.UUID;

/**
 * @author Hien Hoang (hienhoang2702@gmail.com)
 */
public class DeliveryTripDetailModel {

    @Getter
    @AllArgsConstructor
    public static class OrderItems {
        private List<OrderItem> orderItems;
        private Double depotLat;
        private Double depotLng;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrderItem {
        private Integer sequence;
        private UUID deliveryTripDetailId;
        private UUID deliveryTripId;
        private String customerCode;
        private String address;
        private Double lat;
        private Double lng;
        private String productId;
        private String productName;
        private Integer shipmentQuantity;
        private Integer deliveryQuantity;
        private Double weight;
        private String locationCode;

        private String statusId;
    }


    @Getter
    @Setter
    @ToString
    public static class Create {
        //    private UUID deliveryTripId;
        private UUID shipmentItemId;
        //private String shipmentItemSeqId;
        private int deliveryQuantity;
    }
}

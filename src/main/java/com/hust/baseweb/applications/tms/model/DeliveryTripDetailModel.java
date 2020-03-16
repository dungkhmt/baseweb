package com.hust.baseweb.applications.tms.model;

import lombok.*;

import java.util.UUID;

/**
 * @author Hien Hoang (hienhoang2702@gmail.com)
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DeliveryTripDetailModel {

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

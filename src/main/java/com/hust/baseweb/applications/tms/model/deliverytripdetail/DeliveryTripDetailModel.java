package com.hust.baseweb.applications.tms.model.deliverytripdetail;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

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
}

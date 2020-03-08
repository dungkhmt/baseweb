package com.hust.baseweb.applications.tms.model.deliverytrip;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class DeliveryTripLocationItemView {
    private UUID deliveryTripDetailId;
    private UUID shipmentItemId;
    private String productId;
    private String productName;
    private int deliveryQuantity;

}

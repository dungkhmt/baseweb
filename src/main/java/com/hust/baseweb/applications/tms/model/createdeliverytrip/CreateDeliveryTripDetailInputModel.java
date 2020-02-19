package com.hust.baseweb.applications.tms.model.createdeliverytrip;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class CreateDeliveryTripDetailInputModel {
    private UUID deliveryTripId;
    private UUID shipmentId;
    private String shipmentItemSeqId;
    private int deliveryQuantity;
}

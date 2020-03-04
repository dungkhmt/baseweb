package com.hust.baseweb.applications.tms.model.createdeliverytrip;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.UUID;

@Getter
@Setter
@ToString
public class CreateDeliveryTripDetailInputModel {
    //    private UUID deliveryTripId;
    private UUID shipmentItemId;
    //private String shipmentItemSeqId;
    private int deliveryQuantity;
}

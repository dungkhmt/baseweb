package com.hust.baseweb.applications.tms.model.shipmentorder;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ShipmentItemModel {
    private String shipmentId;
    private String shipmentItemSeqId;
    private int quantity;
    private double pallet;
    private String productId;
    private String customerId;
    private String address;
}

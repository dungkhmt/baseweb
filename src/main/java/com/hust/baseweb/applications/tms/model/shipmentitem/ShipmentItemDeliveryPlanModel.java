package com.hust.baseweb.applications.tms.model.shipmentitem;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ShipmentItemDeliveryPlanModel {
    private UUID shipmentItemId;
    private String productName;
    private Integer quantity;
    private Double pallet;
    private String address;
}

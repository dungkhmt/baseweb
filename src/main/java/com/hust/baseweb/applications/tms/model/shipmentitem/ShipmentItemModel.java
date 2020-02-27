package com.hust.baseweb.applications.tms.model.shipmentitem;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class ShipmentItemModel {
    private String shipmentItemId;
    private String shipmentId;
    private Integer quantity;
    private Double pallet;
    private String productId;
    private String customerCode;
    private String locationCode;
}

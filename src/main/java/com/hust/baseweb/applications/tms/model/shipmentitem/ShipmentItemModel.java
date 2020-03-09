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
    private Integer quantity;
    private Double pallet;
    private String productId;
    private String customerCode;
    private String locationCode;
    private String address;
    private String lat;
    private String lng;

    @AllArgsConstructor
    @Getter
    @Setter
    @NoArgsConstructor
    public static class TripDetailSelected {
        private String shipmentItemId;
        private Integer quantity;
    }
}

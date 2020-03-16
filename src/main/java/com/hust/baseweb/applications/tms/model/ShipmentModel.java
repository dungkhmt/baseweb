package com.hust.baseweb.applications.tms.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Hien Hoang (hienhoang2702@gmail.com)
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ShipmentModel {

    private String shipmentId;
    private String shipmentTypeId;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CreateShipmentInputModel {
        private ShipmentItemModel.Create[] shipmentItems;

    }
}

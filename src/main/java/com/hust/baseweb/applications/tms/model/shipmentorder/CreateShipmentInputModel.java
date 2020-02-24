package com.hust.baseweb.applications.tms.model.shipmentorder;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateShipmentInputModel {
    private CreateShipmentItemInputModel[] shipmentItems;

}

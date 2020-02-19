package com.hust.baseweb.applications.tms.model.shipmentorder;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateShipmentInputModel {
    private CreateShipmentItemInputModel[] shipmentItems;

}

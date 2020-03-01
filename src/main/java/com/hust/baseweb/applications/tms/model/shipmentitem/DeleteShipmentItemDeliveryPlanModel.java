package com.hust.baseweb.applications.tms.model.shipmentitem;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DeleteShipmentItemDeliveryPlanModel {
    private String deliveryPlanId;
    private String shipmentItemId;
}

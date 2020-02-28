package com.hust.baseweb.applications.tms.model.shipmentitem;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateShipmentItemDeliveryPlanModel {
    private String deliveryPlanId;
    private List<String> shipmentItemIds;
}

package com.hust.baseweb.applications.tms.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import java.util.UUID;

@Entity
@Getter
@Setter
@IdClass(CompositeShipmentItemDeliveryPlanId.class)
@AllArgsConstructor
@NoArgsConstructor
public class ShipmentItemDeliveryPlan {
    @Id
    @Column(name = "shipment_item_id")
    private UUID shipmentItemId;

    @Id
    @Column(name = "delivery_plan_id")
    private UUID deliveryPlanId;
}

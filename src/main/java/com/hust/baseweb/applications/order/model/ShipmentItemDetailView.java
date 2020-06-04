package com.hust.baseweb.applications.order.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

/**
 * @author Hien Hoang (hienhoang2702@gmail.com)
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ShipmentItemDetailView {

    private UUID shipmentItemId;

    private UUID shipmentId;

    private String facilityId;

    private Integer quantity;

    private Double pallet;

    private String orderId;
}

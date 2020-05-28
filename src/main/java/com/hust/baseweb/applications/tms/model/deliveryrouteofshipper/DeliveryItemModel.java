package com.hust.baseweb.applications.tms.model.deliveryrouteofshipper;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeliveryItemModel {

    private String orderId;
    private String orderItemSeqId;
    private String productId;
    private int quantity;

    public DeliveryItemModel(
        String orderId, String orderItemSeqId,
        String productId, int quantity
    ) {
        super();
        this.orderId = orderId;
        this.orderItemSeqId = orderItemSeqId;
        this.productId = productId;
        this.quantity = quantity;
    }

}

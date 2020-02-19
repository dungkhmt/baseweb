package com.hust.baseweb.applications.order.entity;

import java.io.Serializable;
import java.util.Objects;

public class CompositeOrderItemId implements Serializable {
    private String orderId;
    private String orderItemSeqId;

    public CompositeOrderItemId() {
        super();

    }

    public CompositeOrderItemId(String orderId, String orderItemSeqId) {
        super();
        this.orderId = orderId;
        this.orderItemSeqId = orderItemSeqId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CompositeOrderItemId Id1 = (CompositeOrderItemId) o;
        if (!orderId.equals(Id1.orderId)) {
            return false;
        }
        return orderItemSeqId.equals(Id1.orderItemSeqId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderId, orderItemSeqId);
    }
}

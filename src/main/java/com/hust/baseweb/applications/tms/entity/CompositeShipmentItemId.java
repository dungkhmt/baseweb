package com.hust.baseweb.applications.tms.entity;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;


public class CompositeShipmentItemId implements Serializable {

    private UUID shipmentId;
    private String shipmentItemSeqId;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CompositeShipmentItemId Id1 = (CompositeShipmentItemId) o;
        if (shipmentId != Id1.shipmentId) {
            return false;
        }
        return shipmentItemSeqId.equals(Id1.shipmentItemSeqId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(shipmentId, shipmentItemSeqId);
    }
}

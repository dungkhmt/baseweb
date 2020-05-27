package com.hust.baseweb.applications.logistics.entity;

import java.io.Serializable;
import java.util.Objects;


public class CompositeProductFacilityId implements Serializable {

    private String productId;
    private String facilityId;

    @Override
    public boolean equals(Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CompositeProductFacilityId facilityId = (CompositeProductFacilityId) o;
        if (!productId.equals(facilityId.productId)) {
            return false;
        }
        return this.facilityId.equals(facilityId.facilityId);
    }

    @Override
    public int hashCode() {

        return Objects.hash(productId, facilityId);
    }
}

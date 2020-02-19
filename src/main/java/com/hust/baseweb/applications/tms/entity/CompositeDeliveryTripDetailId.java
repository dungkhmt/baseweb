package com.hust.baseweb.applications.tms.entity;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

public class CompositeDeliveryTripDetailId implements Serializable {
    private UUID deliveryTripId;
    private String deliveryTripItemSeqId;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CompositeDeliveryTripDetailId Id1 = (CompositeDeliveryTripDetailId) o;
        if (deliveryTripId != Id1.deliveryTripId) {
            return false;
        }
        return deliveryTripItemSeqId.equals(Id1.deliveryTripItemSeqId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(deliveryTripId, deliveryTripItemSeqId);
    }

//	  public static void main(String[] args){
//		  String s1 = "abc";
//		  String s2 = "abc";
//		  System.out.println(s1.equals(s2));
//	  }
}

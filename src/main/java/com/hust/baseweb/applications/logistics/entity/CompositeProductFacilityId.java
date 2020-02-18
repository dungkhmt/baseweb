package com.hust.baseweb.applications.logistics.entity;

import java.io.Serializable;
import java.util.Objects;



public class CompositeProductFacilityId implements Serializable{
	private String productId;
	private String facilityId;
	
	@Override
	  public boolean equals(Object o) {
	      if (this == o) return true;
	      if (o == null || getClass() != o.getClass()) return false;
	      CompositeProductFacilityId Id1 = (CompositeProductFacilityId) o;
	      if (!productId.equals(Id1.productId)) return false;
	      return facilityId.equals(Id1.facilityId);
	  }

	  @Override
	  public int hashCode() {
	      return Objects.hash(productId, facilityId);
	  }
}

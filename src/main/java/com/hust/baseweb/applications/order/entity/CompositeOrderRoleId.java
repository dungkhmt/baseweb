package com.hust.baseweb.applications.order.entity;

import java.io.Serializable;
import java.util.Objects;

public class CompositeOrderRoleId implements Serializable {
	private String orderId;
	private String partyId;
	private String roleTypeId;
	public CompositeOrderRoleId() {
		super();
		// TODO Auto-generated constructor stub
	}
	public CompositeOrderRoleId(String orderId, String partyId,
			String roleTypeId) {
		super();
		this.orderId = orderId;
		this.partyId = partyId;
		this.roleTypeId = roleTypeId;
	}
	@Override
	  public boolean equals(Object o) {
	      if (this == o) return true;
	      if (o == null || getClass() != o.getClass()) return false;
	      CompositeOrderRoleId Id1 = (CompositeOrderRoleId) o;
	      if (orderId != Id1.orderId) return false;
	      if(partyId != Id1.partyId) return false;
	      return roleTypeId == Id1.roleTypeId;
	  }

	  @Override
	  public int hashCode() {
	      return Objects.hash(orderId, partyId, roleTypeId);
	  }
}

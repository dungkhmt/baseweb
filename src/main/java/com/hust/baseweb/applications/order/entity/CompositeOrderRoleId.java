package com.hust.baseweb.applications.order.entity;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

public class CompositeOrderRoleId implements Serializable {
	private String orderId;
	private UUID partyId;
	private String roleTypeId;
	public CompositeOrderRoleId() {
		super();
		// TODO Auto-generated constructor stub
	}
	public CompositeOrderRoleId(String orderId, UUID partyId,
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
	      if(partyId.toString() != Id1.partyId.toString()) return false;
	      return roleTypeId == Id1.roleTypeId;
	  }

	  @Override
	  public int hashCode() {
	      return Objects.hash(orderId, partyId, roleTypeId);
	  }
}

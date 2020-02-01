package com.hust.baseweb.applications.order.entity;

import java.io.Serializable;
import java.util.Objects;

public class CompositeOrderItemId implements Serializable {
	private String orderId;
	private String orderItemSeqId;
	public CompositeOrderItemId() {
		super();
		// TODO Auto-generated constructor stub
	}
	public CompositeOrderItemId(String orderId, String orderItemSeqId) {
		super();
		this.orderId = orderId;
		this.orderItemSeqId = orderItemSeqId;
	}
	@Override
	  public boolean equals(Object o) {
	      if (this == o) return true;
	      if (o == null || getClass() != o.getClass()) return false;
	      CompositeOrderItemId Id1 = (CompositeOrderItemId) o;
	      if (orderId != Id1.orderId) return false;
	      return orderItemSeqId == Id1.orderItemSeqId;
	  }

	  @Override
	  public int hashCode() {
	      return Objects.hash(orderId, orderItemSeqId);
	  }
}

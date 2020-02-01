package com.hust.baseweb.applications.order.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ModelCreateOrderInputOrderItem {
	private String productId;
	private int quantity;
	public ModelCreateOrderInputOrderItem() {
		super();
		// TODO Auto-generated constructor stub
	}
	
}

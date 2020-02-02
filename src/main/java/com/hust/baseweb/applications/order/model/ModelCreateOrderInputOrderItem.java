package com.hust.baseweb.applications.order.model;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ModelCreateOrderInputOrderItem {
	private String productId;
	private int quantity;
	private BigDecimal unitPrice;
	private BigDecimal totalItemPrice;
	public ModelCreateOrderInputOrderItem() {
		super();
		// TODO Auto-generated constructor stub
	}
	
}

package com.hust.baseweb.applications.order.model;

import java.math.BigDecimal;
import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderItemDetailView {
	private String orderItemId;
	private String productId;
	private String productName;
	private int quantity;
	private String uom;
	private BigDecimal unitPrice;
	private BigDecimal totalItemPrice;
	
}

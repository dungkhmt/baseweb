package com.hust.baseweb.applications.order.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderDetailView {
	private String orderId;
	private Date orderDate;
	private UUID customerId;
	private String customerName;
	private UUID vendorId;
	private String vendorName;
	private String salesmanLoginId;
	private String salesmanName;
	private BigDecimal total;
	private OrderItemDetailView[] orderItems;
}

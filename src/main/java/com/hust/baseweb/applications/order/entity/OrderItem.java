package com.hust.baseweb.applications.order.entity;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import com.hust.baseweb.applications.logistics.entity.Product;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@IdClass(CompositeOrderItemId.class)
public class OrderItem {
	@Id
	@Column(name = "order_id")
	private String orderId;
	
	@Id
	@Column(name="order_item_seq_id")
	private String orderItemSeqId;
	
	@JoinColumn(name="product_id", referencedColumnName="product_id")
	@ManyToOne(fetch = FetchType.EAGER)
	private Product product;
	
	@Column(name="unit_price")
	private BigDecimal unitPrice;
	
	@Column(name="quantity")
	private int quantity;
}

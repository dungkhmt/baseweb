package com.hust.baseweb.applications.order.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class OrderItemType {
	@Id
	@Column(name="order_item_type_id")
	private String orderItemTypeId;
	
}

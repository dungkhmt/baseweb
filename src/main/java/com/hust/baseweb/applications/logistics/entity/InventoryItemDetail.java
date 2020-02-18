package com.hust.baseweb.applications.logistics.entity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class InventoryItemDetail {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="inventory_item_detail_id")
	private UUID inventoryItemDetailId;
	
	@JoinColumn(name="inventory_item_id", referencedColumnName="inventory_item_id")
	@ManyToOne(fetch=FetchType.LAZY)
	private InventoryItem inventoryItem;
	
	@Column(name="effective_date")
	private Date effectiveDate;
	
	@Column(name="quantity_on_hand_diff")
	private int quantityOnHandDiff;
	
	@Column(name="order_id")
	private UUID orderId;
	
	@Column(name="order_item_seq_id")
	private String orderItemSeqId;
	
	
}

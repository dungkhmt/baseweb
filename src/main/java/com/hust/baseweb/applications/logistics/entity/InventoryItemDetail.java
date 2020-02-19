package com.hust.baseweb.applications.logistics.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Entity
@Getter
@Setter
public class InventoryItemDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "inventory_item_detail_id")
    private UUID inventoryItemDetailId;

    @JoinColumn(name = "inventory_item_id", referencedColumnName = "inventory_item_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private InventoryItem inventoryItem;

    @Column(name = "effective_date")
    private Date effectiveDate;

    @Column(name = "quantity_on_hand_diff")
    private int quantityOnHandDiff;

    @Column(name = "order_id")
    private UUID orderId;

    @Column(name = "order_item_seq_id")
    private String orderItemSeqId;


}

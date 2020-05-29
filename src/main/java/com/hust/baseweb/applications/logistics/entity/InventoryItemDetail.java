package com.hust.baseweb.applications.logistics.entity;

import com.hust.baseweb.applications.logistics.model.InventoryModel;
import com.hust.baseweb.applications.order.entity.OrderItem;
import com.hust.baseweb.utils.Constant;
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

    @Column(name = "order_id", insertable = false, updatable = false)
    private String orderId;

    @JoinColumn(name = "order_id", referencedColumnName = "order_id")
    @JoinColumn(name = "order_item_seq_id", referencedColumnName = "order_item_seq_id")
    @ManyToOne(fetch = FetchType.EAGER)
    private OrderItem orderItem;

    public InventoryModel.ExportDetail toInventoryExportDetail() {
        return new InventoryModel.ExportDetail(
            inventoryItemDetailId.toString(), orderItem.getOrderId(), Constant.DATE_FORMAT.format(effectiveDate)
        );
    }
}

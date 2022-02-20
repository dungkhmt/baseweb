package com.hust.baseweb.applications.order.entity;

import com.hust.baseweb.applications.logistics.entity.Product;
import com.hust.baseweb.applications.logistics.model.InventoryModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@IdClass(CompositeOrderItemId.class)
public class OrderItem {

    @Id
    @Column(name = "order_id")
    private String orderId;

    @Id
    @Column(name = "order_item_seq_id")
    private String orderItemSeqId;

    @JoinColumn(name = "product_id", referencedColumnName = "product_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Product product;

    @Column(name = "unit_price")
    private Double unitPrice;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "status_id")
    private String statusId;

    @Column(name = "created_stamp")
    private Date createdStamp;

    @Column(name = "last_updated_stamp")
    private Date lastUpdatedStamp;

    private Integer exportedQuantity = 0;

    public InventoryModel.OrderItem toOrderItemModel(int exportedQuantity, int inventoryQuantity) {
        return new InventoryModel.OrderItem(
            product.getProductId(),
            product.getProductName(),
            quantity,
            exportedQuantity,
            inventoryQuantity,
            orderId,
            orderItemSeqId
        );
    }
}

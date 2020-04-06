package com.hust.baseweb.applications.order.entity;

import com.hust.baseweb.applications.logistics.entity.Product;
import com.hust.baseweb.applications.logistics.model.InventoryModel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@IdClass(CompositeOrderItemId.class)
public class OrderItem {

    @Id
    @Column(name = "order_id")
    private String orderId;

    @Id
    @Column(name = "order_item_seq_id")
    private String orderItemSeqId;

    @JoinColumn(name = "product_id", referencedColumnName = "product_id")
    @ManyToOne(fetch = FetchType.EAGER)
    private Product product;

    @Column(name = "unit_price")
    private Double unitPrice;

    @Column(name = "quantity")
    private Integer quantity;

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

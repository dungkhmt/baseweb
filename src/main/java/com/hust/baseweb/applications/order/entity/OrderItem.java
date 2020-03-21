package com.hust.baseweb.applications.order.entity;

import com.hust.baseweb.applications.logistics.entity.Facility;
import com.hust.baseweb.applications.logistics.entity.Product;
import com.hust.baseweb.applications.logistics.model.InventoryModel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;

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

    @JoinColumn(name = "facility_id", referencedColumnName = "facility_id")
    @ManyToOne(fetch = FetchType.EAGER)
    private Facility facility;

    @JoinColumn(name = "product_id", referencedColumnName = "product_id")
    @ManyToOne(fetch = FetchType.EAGER)
    private Product product;

    @Column(name = "unit_price")
    private BigDecimal unitPrice;

    @Column(name = "quantity")
    private int quantity;

    public InventoryModel.OrderItem toOrderItemModel(int exportedQuantity) {
        return new InventoryModel.OrderItem(
                product.getProductId(),
                product.getProductName(),
                quantity,
                exportedQuantity,
                orderId
        );
    }
}

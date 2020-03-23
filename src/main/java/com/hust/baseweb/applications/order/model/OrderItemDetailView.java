package com.hust.baseweb.applications.order.model;

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
    private Double unitPrice;
    private Double totalItemPrice;

}

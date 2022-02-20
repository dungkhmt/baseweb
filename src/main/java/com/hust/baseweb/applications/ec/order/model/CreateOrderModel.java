package com.hust.baseweb.applications.ec.order.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateOrderModel {
    private String orderId;
    private String productId;
    private String storeId;
    private Integer quantity;
}

package com.hust.baseweb.applications.ec.order.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderResult {
    private String orderId;
    private String productName;
    private Integer quantity;
    private String statusId;
}

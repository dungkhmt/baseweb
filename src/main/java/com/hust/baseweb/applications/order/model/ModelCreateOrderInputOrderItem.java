package com.hust.baseweb.applications.order.model;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ModelCreateOrderInputOrderItem {
    private String productId;
    private int quantity;
    //private BigDecimal unitPrice;
    //private BigDecimal totalItemPrice;

    public ModelCreateOrderInputOrderItem() {
        super();

    }

}

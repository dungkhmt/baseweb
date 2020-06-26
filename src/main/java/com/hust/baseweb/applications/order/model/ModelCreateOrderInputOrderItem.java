package com.hust.baseweb.applications.order.model;

import lombok.Getter;
import lombok.Setter;

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

package com.hust.baseweb.applications.order.entity.aggregation;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class POrderItem {

    private String productId;
    private String productName;
    private int quantity;
    private String uomId;
    private String uomName;
    private int unitPrice;
}

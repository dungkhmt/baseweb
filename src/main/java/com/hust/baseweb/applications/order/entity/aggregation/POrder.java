package com.hust.baseweb.applications.order.entity.aggregation;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.UUID;

@Getter
@Setter
public class POrder {

    private String orderId;
    private Date orderDate;
    private UUID customerId;
    private String customerName;
    private UUID vendorId;
    private String vendorName;
    private Double grandTotal;

    private POrderItem[] orderItems;

}

package com.hust.baseweb.applications.order.entity.aggregation;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class ACustomerRevenue {
    private UUID customerId;
    private double revenue;
}

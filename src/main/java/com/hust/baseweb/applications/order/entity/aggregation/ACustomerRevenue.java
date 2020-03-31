package com.hust.baseweb.applications.order.entity.aggregation;

import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ACustomerRevenue {
	private UUID customerId;
	private double revenue;
}

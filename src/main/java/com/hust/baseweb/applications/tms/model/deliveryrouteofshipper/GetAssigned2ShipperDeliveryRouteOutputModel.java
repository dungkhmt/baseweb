package com.hust.baseweb.applications.tms.model.deliveryrouteofshipper;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetAssigned2ShipperDeliveryRouteOutputModel {
	private List<DeliveryCustomerModel> deliveryCustomers;

	public GetAssigned2ShipperDeliveryRouteOutputModel(
			List<DeliveryCustomerModel> deliveryCustomers) {
		super();
		this.deliveryCustomers = deliveryCustomers;
	}
	
}

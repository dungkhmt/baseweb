package com.hust.baseweb.applications.tms.model.deliveryrouteofshipper;

import java.util.List;

import com.hust.baseweb.applications.customer.entity.PartyCustomer;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeliveryCustomerModel {
	private PartyCustomer customer;
	private List<DeliveryItemModel> deliveryItems;
	public DeliveryCustomerModel(PartyCustomer customer,
			List<DeliveryItemModel> deliveryItems) {
		super();
		this.customer = customer;
		this.deliveryItems = deliveryItems;
	}
	
}

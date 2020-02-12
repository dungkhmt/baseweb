package com.hust.baseweb.applications.order.model;



import java.util.List;

import com.hust.baseweb.applications.customer.entity.PartyCustomer;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class GetListPartyCustomerOutputModel {
	private List<PartyCustomer> customers;

	public GetListPartyCustomerOutputModel(List<PartyCustomer> customers) {
		super();
		this.customers = customers;
	}
	
}

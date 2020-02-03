package com.hust.baseweb.applications.order.model;

import com.hust.baseweb.applications.order.entity.PartyCustomer;

import java.util.List;

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

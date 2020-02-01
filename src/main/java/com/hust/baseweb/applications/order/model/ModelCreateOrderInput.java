package com.hust.baseweb.applications.order.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class ModelCreateOrderInput {
	private String partyCustomerId;
	private ModelCreateOrderInputOrderItem[] orderItems;
	private String salesChannelId;
	private String facilityId;
	private String salesmanId;
	public ModelCreateOrderInput() {
		super();
		// TODO Auto-generated constructor stub
	}
	
}

package com.hust.baseweb.applications.order.model;

import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class ModelCreateOrderInput {
	private UUID partyCustomerId;
	private ModelCreateOrderInputOrderItem[] orderItems;
	private String salesChannelId;
	private String facilityId;
	private String salesmanId;
	public ModelCreateOrderInput() {
		super();
		// TODO Auto-generated constructor stub
	}
	
}

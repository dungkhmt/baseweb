package com.hust.baseweb.applications.logistics.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExportInventoryItemInputModel {
	private String productId;
	private String facilityId;
	private int quantity;
}

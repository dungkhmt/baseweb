package com.hust.baseweb.applications.logistics.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ImportInventoryItemsInputModel {
	private ImportInventoryItemInputModel[] inventoryItems;

	public ImportInventoryItemsInputModel() {
		super();
		// TODO Auto-generated constructor stub
	}
	
}

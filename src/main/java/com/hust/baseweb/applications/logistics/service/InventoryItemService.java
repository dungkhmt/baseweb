package com.hust.baseweb.applications.logistics.service;

import com.hust.baseweb.applications.logistics.entity.InventoryItem;
import com.hust.baseweb.applications.logistics.model.ImportInventoryItemInputModel;

public interface InventoryItemService {
	public InventoryItem save(ImportInventoryItemInputModel input);
}

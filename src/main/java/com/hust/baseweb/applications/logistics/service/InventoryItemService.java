package com.hust.baseweb.applications.logistics.service;

import com.hust.baseweb.applications.logistics.entity.InventoryItem;
import com.hust.baseweb.applications.logistics.model.ExportInventoryItemsInputModel;
import com.hust.baseweb.applications.logistics.model.ImportInventoryItemInputModel;

public interface InventoryItemService {
    InventoryItem save(ImportInventoryItemInputModel input);

    String exportInventoryItems(ExportInventoryItemsInputModel inventoryItems);
}

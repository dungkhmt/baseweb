package com.hust.baseweb.applications.logistics.service;

import com.hust.baseweb.applications.logistics.entity.InventoryItem;
import com.hust.baseweb.applications.logistics.entity.InventoryItemDetail;
import org.springframework.stereotype.Service;

@Service
public interface InventoryItemDetailService {
    InventoryItemDetail save(InventoryItem inventoryItem, int qtyOnHandDiff);

}

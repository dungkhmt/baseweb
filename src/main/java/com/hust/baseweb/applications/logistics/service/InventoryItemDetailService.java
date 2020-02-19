package com.hust.baseweb.applications.logistics.service;

import com.hust.baseweb.applications.logistics.entity.InventoryItemDetail;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Service
public interface InventoryItemDetailService {
    InventoryItemDetail save(UUID inventoryItemId, int qtyOnHandDiff, Date effectiveDate);

}

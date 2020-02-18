package com.hust.baseweb.applications.logistics.service;

import java.util.Date;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.hust.baseweb.applications.logistics.entity.InventoryItemDetail;

@Service
public interface InventoryItemDetailService {
	InventoryItemDetail save(UUID inventoryItemId, int qtyOnHandDiff, Date effectiveDate);
	
}

package com.hust.baseweb.applications.logistics.repo;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.hust.baseweb.applications.logistics.entity.InventoryItem;

public interface InventoryItemRepo extends PagingAndSortingRepository<InventoryItem,UUID>{
	public InventoryItem findByInventoryItemId(UUID inventoryItemId);
	public List<InventoryItem> findAll();
	//public List<InventoryItem> findAllByProductIdAndFacilityId(String productId, String facilityId); NOT ALLOWED
}

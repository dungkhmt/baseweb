package com.hust.baseweb.applications.logistics.repo;

import com.hust.baseweb.applications.logistics.entity.InventoryItem;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.UUID;

public interface InventoryItemRepo extends PagingAndSortingRepository<InventoryItem, UUID> {
    public InventoryItem findByInventoryItemId(UUID inventoryItemId);

    public List<InventoryItem> findAll();
    //public List<InventoryItem> findAllByProductIdAndFacilityId(String productId, String facilityId); NOT ALLOWED
}

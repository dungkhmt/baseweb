package com.hust.baseweb.applications.logistics.repo;

import com.hust.baseweb.applications.logistics.entity.Facility;
import com.hust.baseweb.applications.logistics.entity.InventoryItem;
import com.hust.baseweb.applications.logistics.entity.Product;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.UUID;

public interface InventoryItemRepo extends PagingAndSortingRepository<InventoryItem, UUID> {
    InventoryItem findByInventoryItemId(UUID inventoryItemId);

    @NotNull
    List<InventoryItem> findAll();
    //public List<InventoryItem> findAllByProductIdAndFacilityId(String productId, String facilityId); NOT ALLOWED

    List<InventoryItem> findAllByProductInAndFacilityInAndQuantityOnHandTotalGreaterThan(List<Product> products,
                                                                                         List<Facility> facilities,
                                                                                         int quantity);
}

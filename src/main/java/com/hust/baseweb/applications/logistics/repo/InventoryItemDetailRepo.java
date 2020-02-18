package com.hust.baseweb.applications.logistics.repo;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.hust.baseweb.applications.logistics.entity.InventoryItemDetail;
import java.util.UUID;

public interface InventoryItemDetailRepo extends
		PagingAndSortingRepository<InventoryItemDetail, UUID> {

}

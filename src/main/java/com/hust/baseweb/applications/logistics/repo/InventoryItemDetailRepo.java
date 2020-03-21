package com.hust.baseweb.applications.logistics.repo;

import com.hust.baseweb.applications.logistics.entity.InventoryItemDetail;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.UUID;

public interface InventoryItemDetailRepo extends
        PagingAndSortingRepository<InventoryItemDetail, UUID> {

    List<InventoryItemDetail> findAllByOrderIdInAndOrderItemSeqIdIn(List<String> orderIds, List<String> orderItemSeqIds);
}

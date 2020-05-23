package com.hust.baseweb.applications.logistics.repo;

import com.hust.baseweb.applications.logistics.entity.InventoryItemDetail;
import com.hust.baseweb.applications.order.entity.OrderItem;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.UUID;

public interface InventoryItemDetailRepo extends
    PagingAndSortingRepository<InventoryItemDetail, UUID> {

    List<InventoryItemDetail> findAllByOrderItemIn(List<OrderItem> orderItems);
}

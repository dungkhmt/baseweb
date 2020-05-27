package com.hust.baseweb.applications.logistics.service;

import com.hust.baseweb.applications.logistics.entity.InventoryItem;
import com.hust.baseweb.applications.logistics.entity.InventoryItemDetail;
import com.hust.baseweb.applications.order.entity.OrderItem;
import org.springframework.stereotype.Service;

@Service
public interface InventoryItemDetailService {

    InventoryItemDetail createInventoryItemDetail(
        InventoryItem inventoryItem,
        int qtyOnHandDiff,
        OrderItem orderItem);

}

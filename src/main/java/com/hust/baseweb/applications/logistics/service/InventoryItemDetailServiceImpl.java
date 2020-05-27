package com.hust.baseweb.applications.logistics.service;

import com.hust.baseweb.applications.logistics.entity.InventoryItem;
import com.hust.baseweb.applications.logistics.entity.InventoryItemDetail;
import com.hust.baseweb.applications.order.entity.OrderItem;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
@Log4j2
public class InventoryItemDetailServiceImpl implements InventoryItemDetailService {

    @NotNull
    public InventoryItemDetail createInventoryItemDetail(
        InventoryItem inventoryItem,
        int qtyOnHandDiff,
        OrderItem orderItem) {

        Date effectiveDate = new Date();
        InventoryItemDetail inventoryItemDetail = new InventoryItemDetail();
        inventoryItemDetail.setEffectiveDate(effectiveDate);
        inventoryItemDetail.setInventoryItem(inventoryItem);
        inventoryItemDetail.setQuantityOnHandDiff(qtyOnHandDiff);
        inventoryItemDetail.setOrderItem(orderItem);
        return inventoryItemDetail;
    }

}

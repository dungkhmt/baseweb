package com.hust.baseweb.applications.logistics.service;

import com.hust.baseweb.applications.logistics.entity.Facility;
import com.hust.baseweb.applications.logistics.entity.InventoryItem;
import com.hust.baseweb.applications.logistics.entity.Receipt;
import com.hust.baseweb.applications.logistics.entity.ReceiptItem;

import java.util.List;

/**
 * @author Hien Hoang (hienhoang2702@gmail.com)
 */
public interface ReceiptService {
    Receipt create(Facility facility);

    List<ReceiptItem> createReceiptItems(Receipt receipt,
                                         List<InventoryItem> inventoryItems);
}

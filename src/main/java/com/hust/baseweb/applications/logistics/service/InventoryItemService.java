package com.hust.baseweb.applications.logistics.service;

import com.hust.baseweb.applications.logistics.entity.InventoryItem;
import com.hust.baseweb.applications.logistics.model.ExportInventoryItemsInputModel;
import com.hust.baseweb.applications.logistics.model.ImportInventoryItemInputModel;
import com.hust.baseweb.applications.logistics.model.InventoryModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface InventoryItemService {
    InventoryItem importInventoryItem(ImportInventoryItemInputModel input);

    String exportInventoryItems(ExportInventoryItemsInputModel inventoryItems);

    Page<InventoryModel.OrderHeader> getInventoryOrderHeaderPage(Pageable page);

    List<InventoryModel.OrderItem> getInventoryOrderHeaderDetail(String orderId);

    List<InventoryModel.ExportDetail> getInventoryExportList(String facilityId);

    List<InventoryModel.ProductFacility> getInventoryList(String facilityId);
}

package com.hust.baseweb.applications.logistics.service;

import com.hust.baseweb.applications.logistics.entity.InventoryItem;
import com.hust.baseweb.applications.logistics.model.ExportInventoryItemsInputModel;
import com.hust.baseweb.applications.logistics.model.ImportInventoryItemsInputModel;
import com.hust.baseweb.applications.logistics.model.InventoryModel;
import com.hust.baseweb.entity.UserLogin;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface InventoryItemService {

    List<InventoryItem> importInventoryItems(ImportInventoryItemsInputModel inventoryItems);

    String exportInventoryItems(ExportInventoryItemsInputModel inventoryItems, UserLogin userLogin);

    Page<InventoryModel.OrderHeader> getInventoryOrderHeaderPage(Pageable page);

    List<InventoryModel.OrderItem> getInventoryOrderHeaderDetail(String facilityId, String orderId);

    List<InventoryModel.ExportDetail> getInventoryExportList(
        String facilityId,
        UserLogin userLogin
    );

    List<InventoryModel.ProductFacility> getInventoryList(String facilityId);
}

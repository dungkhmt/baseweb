package com.hust.baseweb.applications.specialpurpose.saleslogmongo.service;

import com.hust.baseweb.applications.specialpurpose.saleslogmongo.document.PurchaseOrder;
import com.hust.baseweb.applications.specialpurpose.saleslogmongo.model.CreatePurchaseOrderInputModel;
import com.hust.baseweb.applications.specialpurpose.saleslogmongo.model.GetInventoryItemOutputModel;

/**
 * @author Hien Hoang (hienhoang2702@gmail.com)
 */
public interface LogisticService {

    PurchaseOrder createPurchaseOrder(CreatePurchaseOrderInputModel input);

    GetInventoryItemOutputModel getInventoryItems(String facilityId);
}

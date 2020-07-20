package com.hust.baseweb.applications.specialpurpose.saleslogmongo.service;

import com.hust.baseweb.applications.specialpurpose.saleslogmongo.model.GetInventoryItemOutputModel;

/**
 * @author Hien Hoang (hienhoang2702@gmail.com)
 */
public interface LogisticService {

    GetInventoryItemOutputModel getInventoryItems(String facilityId);
}

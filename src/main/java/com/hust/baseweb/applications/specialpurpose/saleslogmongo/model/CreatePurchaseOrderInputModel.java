package com.hust.baseweb.applications.specialpurpose.saleslogmongo.model;

import com.hust.baseweb.applications.specialpurpose.saleslogmongo.document.OrderItem;

import java.util.Date;

public class CreatePurchaseOrderInputModel {
    private String fromProviderId;
    private Date orderDate;
    private String userLoginId;
    private String toFacilityId;
    private OrderItem[] orderItem;
}

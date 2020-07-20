package com.hust.baseweb.applications.specialpurpose.saleslogmongo.document;

import java.util.Date;

public class PurchaseOrder {
    private String orderId;
    private Organization fromProvider;
    private Person poStaff;
    private Date orderDate;
    private Facility toFacility;
    private OrderItem[] orderItems;
}

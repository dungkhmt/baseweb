package com.hust.baseweb.applications.specialpurpose.saleslogmongo.document;

import java.util.Date;
import java.util.List;

public class SalesOrder {
    private String orderId;
    private Organization customer;
    private Date orderDate;
    private Person salesman;
    private Facility fromFacility;
    private List<OrderItem> orderItems;

}

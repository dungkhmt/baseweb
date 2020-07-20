package com.hust.baseweb.applications.specialpurpose.saleslogmongo.model;

import com.hust.baseweb.applications.specialpurpose.saleslogmongo.document.OrderItem;

import java.util.Date;

public class CreateSalesOrderInputModel {
    private String userLoginId;// userlogin of salesman'
    private Date orderDate;
    private String customerId;
    private String fromFacilityId;
    private OrderItem[] orderItems;

}

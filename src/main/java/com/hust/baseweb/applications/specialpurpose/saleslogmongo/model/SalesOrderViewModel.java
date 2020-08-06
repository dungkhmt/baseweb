package com.hust.baseweb.applications.specialpurpose.saleslogmongo.model;

import com.hust.baseweb.applications.specialpurpose.saleslogmongo.document.Organization;
import com.hust.baseweb.entity.Person;
import com.hust.baseweb.entity.UserLogin;

import java.util.List;

public class SalesOrderViewModel {
    private Organization customer;
    private FacilityModel fromFacility;
    private Person salesman;
    private List<OrderItemViewModel> orderItems;
}

package com.hust.baseweb.applications.specialpurpose.saleslogmongo.service;

import com.hust.baseweb.applications.specialpurpose.saleslogmongo.document.Customer;
import com.hust.baseweb.applications.specialpurpose.saleslogmongo.document.SalesOrder;
import com.hust.baseweb.applications.specialpurpose.saleslogmongo.model.CreateSalesOrderInputModel;
import com.hust.baseweb.applications.specialpurpose.saleslogmongo.model.CustomerModel;

import java.util.List;

/**
 * @author Hien Hoang (hienhoang2702@gmail.com)
 */
public interface SalesService {

    SalesOrder createSalesOrder(CreateSalesOrderInputModel input);

    Customer createCusstomerOfSalesman(String salesmanId, String customerName, String address);
    List<CustomerModel> getCustomersOfSalesman(String salesmanId);

    void deleteAllRunningData();
}

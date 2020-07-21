package com.hust.baseweb.applications.specialpurpose.saleslogmongo.service;

import com.hust.baseweb.applications.specialpurpose.saleslogmongo.document.SalesOrder;
import com.hust.baseweb.applications.specialpurpose.saleslogmongo.model.CreateSalesOrderInputModel;

/**
 * @author Hien Hoang (hienhoang2702@gmail.com)
 */
public interface SalesService {

    SalesOrder createSalesOrder(CreateSalesOrderInputModel input);
}

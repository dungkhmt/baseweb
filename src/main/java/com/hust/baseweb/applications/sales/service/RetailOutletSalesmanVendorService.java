package com.hust.baseweb.applications.sales.service;

import com.hust.baseweb.applications.sales.entity.RetailOutletSalesmanVendor;
import com.hust.baseweb.applications.sales.model.retailoutletsalesmandistributor.RetailOutletSalesmanDistributorInputModel;
import org.springframework.stereotype.Service;

@Service
public interface RetailOutletSalesmanVendorService {
    RetailOutletSalesmanVendor save(RetailOutletSalesmanDistributorInputModel input);
}

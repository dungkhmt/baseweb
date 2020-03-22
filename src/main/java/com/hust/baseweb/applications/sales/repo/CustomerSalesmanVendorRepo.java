package com.hust.baseweb.applications.sales.repo;

import com.hust.baseweb.applications.sales.entity.CustomerSalesmanVendor;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface CustomerSalesmanVendorRepo extends CrudRepository<CustomerSalesmanVendor, UUID> {
    CustomerSalesmanVendor findByCustomerSalesmanVendorId(UUID customerSalesmanVendorId);
}

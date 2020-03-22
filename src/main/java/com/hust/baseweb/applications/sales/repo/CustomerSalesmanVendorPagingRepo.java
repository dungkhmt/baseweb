package com.hust.baseweb.applications.sales.repo;

import com.hust.baseweb.applications.sales.entity.CustomerSalesmanVendor;
import com.hust.baseweb.applications.sales.entity.PartySalesman;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public interface CustomerSalesmanVendorPagingRepo extends PagingAndSortingRepository<CustomerSalesmanVendor, UUID> {
    Page<CustomerSalesmanVendor> findByPartySalesman(PartySalesman partySalesman, Pageable pageable);
    Page<CustomerSalesmanVendor> findByPartySalesmanAndThruDate(PartySalesman partySalesman, Date thruDate, Pageable pageable);
    
}

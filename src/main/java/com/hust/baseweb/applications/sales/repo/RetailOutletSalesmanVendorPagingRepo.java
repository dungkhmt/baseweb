package com.hust.baseweb.applications.sales.repo;

import com.hust.baseweb.applications.sales.entity.PartySalesman;
import com.hust.baseweb.applications.sales.entity.RetailOutletSalesmanVendor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Date;
import java.util.UUID;

public interface RetailOutletSalesmanVendorPagingRepo
    extends PagingAndSortingRepository<RetailOutletSalesmanVendor, UUID> {

    Page<RetailOutletSalesmanVendor> findByPartySalesman(PartySalesman partySalesman, Pageable pageable);

    Page<RetailOutletSalesmanVendor> findByPartySalesmanAndThruDate(
        PartySalesman partySalesman,
        Date thruDate,
        Pageable pageable
    );

}

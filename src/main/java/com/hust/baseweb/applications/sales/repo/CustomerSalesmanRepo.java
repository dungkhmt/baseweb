package com.hust.baseweb.applications.sales.repo;

import com.hust.baseweb.applications.sales.entity.CustomerSalesman;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.UUID;

public interface CustomerSalesmanRepo extends PagingAndSortingRepository<CustomerSalesman, UUID> {

    List<CustomerSalesman> findAll();

}

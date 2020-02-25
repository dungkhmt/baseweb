package com.hust.baseweb.applications.sales.repo;

import java.util.List;
import java.util.UUID;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.hust.baseweb.applications.sales.entity.CustomerSalesman;

public interface CustomerSalesmanRepo extends PagingAndSortingRepository<CustomerSalesman, UUID> {
	public List<CustomerSalesman> findAll();
}

package com.hust.baseweb.applications.sales.repo;

import java.util.UUID;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.hust.baseweb.applications.sales.entity.PartySalesman;

public interface PartySalesmanRepo extends PagingAndSortingRepository<PartySalesman, UUID> {

}

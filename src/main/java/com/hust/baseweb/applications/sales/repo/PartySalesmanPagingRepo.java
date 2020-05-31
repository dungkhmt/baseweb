package com.hust.baseweb.applications.sales.repo;

import com.hust.baseweb.applications.sales.entity.PartySalesman;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.UUID;

public interface PartySalesmanPagingRepo extends PagingAndSortingRepository<PartySalesman, UUID> {

}

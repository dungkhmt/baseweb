package com.hust.baseweb.applications.order.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.hust.baseweb.applications.order.entity.OrderHeader;

import java.awt.print.Pageable;
import java.util.UUID;
public interface POrderRepo extends PagingAndSortingRepository<OrderHeader, UUID> {
	//Page<OrderHeader> findAll(Pageable page);
}

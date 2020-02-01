package com.hust.baseweb.applications.order.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hust.baseweb.applications.order.entity.OrderHeader;

public interface OrderRepo extends JpaRepository<OrderHeader, String>{
	
}

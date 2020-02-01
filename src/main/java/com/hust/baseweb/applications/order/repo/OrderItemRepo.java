package com.hust.baseweb.applications.order.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hust.baseweb.applications.order.entity.OrderItem;

public interface OrderItemRepo extends JpaRepository<OrderItem, String> {
	
}

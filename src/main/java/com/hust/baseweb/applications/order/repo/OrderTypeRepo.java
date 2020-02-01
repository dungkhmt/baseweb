package com.hust.baseweb.applications.order.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hust.baseweb.applications.order.entity.OrderType;

public interface OrderTypeRepo extends JpaRepository<OrderType, String>{
	public OrderType findByOrderTypeId(String orderTypeId);
}

package com.hust.baseweb.applications.order.repo;

import java.util.List;

import com.hust.baseweb.applications.order.entity.OrderRole;

import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRoleRepo extends JpaRepository<OrderRole, String> {
	List<OrderRole> findByOrderId(String orderId);
}

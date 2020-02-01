package com.hust.baseweb.applications.order.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hust.baseweb.applications.order.entity.OrderStatus;

public interface OrderStatusRepo extends JpaRepository<OrderStatus, String>{

}

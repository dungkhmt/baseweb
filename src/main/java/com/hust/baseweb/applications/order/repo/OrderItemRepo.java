package com.hust.baseweb.applications.order.repo;

import com.hust.baseweb.applications.order.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepo extends JpaRepository<OrderItem, String> {

}

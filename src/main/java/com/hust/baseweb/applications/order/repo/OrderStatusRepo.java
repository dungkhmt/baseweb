package com.hust.baseweb.applications.order.repo;

import com.hust.baseweb.applications.order.entity.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderStatusRepo extends JpaRepository<OrderStatus, String> {

}

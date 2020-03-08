package com.hust.baseweb.applications.order.repo;

import com.hust.baseweb.applications.order.entity.OrderHeader;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepo extends JpaRepository<OrderHeader, String> {
    OrderHeader findByOrderId(String orderId);
}

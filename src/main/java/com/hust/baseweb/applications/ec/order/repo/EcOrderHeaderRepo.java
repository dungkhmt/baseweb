package com.hust.baseweb.applications.ec.order.repo;

import com.hust.baseweb.applications.order.entity.OrderHeader;
import org.springframework.data.jpa.repository.JpaRepository;


public interface EcOrderHeaderRepo extends JpaRepository<OrderHeader, String> {
    OrderHeader findByOrderId(String orderId);
}

package com.hust.baseweb.applications.order.repo;

import com.hust.baseweb.applications.order.entity.OrderHeader;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderHeaderRepo extends JpaRepository<OrderHeader, String> {
    OrderHeader findByOrderId(String orderId);

    List<OrderHeader> findAllByOrderIdIn(List<String> orderIds);
}

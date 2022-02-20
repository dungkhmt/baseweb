package com.hust.baseweb.applications.ec.order.repo;

import com.hust.baseweb.applications.order.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EcOrderItemRepo extends JpaRepository<OrderItem, String> {
    List<OrderItem> findAllByUserId(String userId);
    OrderItem findByOrderId(String orderId);
}


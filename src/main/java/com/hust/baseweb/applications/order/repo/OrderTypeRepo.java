package com.hust.baseweb.applications.order.repo;

import com.hust.baseweb.applications.order.entity.OrderType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderTypeRepo extends JpaRepository<OrderType, String> {
    public OrderType findByOrderTypeId(String orderTypeId);
}

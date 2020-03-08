package com.hust.baseweb.applications.order.repo;

import com.hust.baseweb.applications.order.entity.OrderRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRoleRepo extends JpaRepository<OrderRole, String> {
    List<OrderRole> findByOrderId(String orderId);
}

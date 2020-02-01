package com.hust.baseweb.applications.order.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hust.baseweb.applications.order.entity.OrderRole;

public interface OrderRoleRepo extends JpaRepository<OrderRole, String> {

}

package com.hust.baseweb.applications.order.repo;

import com.hust.baseweb.applications.order.entity.OrderHeader;
import com.hust.baseweb.applications.order.entity.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface OrderStatusRepo extends JpaRepository<OrderStatus, String> {

    List<OrderStatus> findAllByOrderIn(Collection<OrderHeader> order);

    void deleteAllByOrderIn(Collection<OrderHeader> order);
}

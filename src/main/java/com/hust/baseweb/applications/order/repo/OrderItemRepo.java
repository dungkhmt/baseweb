package com.hust.baseweb.applications.order.repo;

import com.hust.baseweb.applications.logistics.entity.Product;
import com.hust.baseweb.applications.order.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderItemRepo extends JpaRepository<OrderItem, String> {

    List<OrderItem> findAllByOrderId(String orderId);

    List<OrderItem> findAllByOrderIdInAndOrderItemSeqIdIn(List<String> orderIds, List<String> orderItemSeqIds);

    List<OrderItem> findAllByProductAndOrderIdIn(Product product, List<String> orderIds);

    List<OrderItem> findAllByOrderIdIn(List<String> orderIds);
}

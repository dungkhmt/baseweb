package com.hust.baseweb.applications.order.repo;

import com.hust.baseweb.applications.order.entity.OrderHeader;
import com.hust.baseweb.applications.order.entity.OrderType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.Date;
import java.util.List;

public interface OrderHeaderRepo extends JpaRepository<OrderHeader, String> {

    OrderHeader findByOrderId(String orderId);

    List<OrderHeader> findAllByOrderIdIn(List<String> orderIds);

    List<OrderHeader> findAllByOrderDateBetween(Date from, Date to);

    List<OrderHeader> findAllByOrderIdInAndOrderDateBetween(
        List<String> orderIds,
        Date fromDate,
        Date toDate
    );

    List<OrderHeader> findAllByOrderType(OrderType orderType);

    void deleteAllByOrderIdIn(Collection<String> orderId);
}

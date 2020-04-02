package com.hust.baseweb.applications.order.service;

import com.hust.baseweb.applications.customer.entity.PartyCustomer;
import com.hust.baseweb.applications.order.entity.OrderItem;

import java.time.LocalDate;
import java.util.List;
import java.util.function.Function;

/**
 * @author Hien Hoang (hienhoang2702@gmail.com)
 */
public interface RevenueService {
    void updateRevenue(List<OrderItem> orderItems,
                       Function<OrderItem, PartyCustomer> orderItemToCustomerFunction,
                       Function<OrderItem, LocalDate> orderItemToDateFunction);
}

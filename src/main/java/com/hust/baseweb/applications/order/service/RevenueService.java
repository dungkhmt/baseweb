package com.hust.baseweb.applications.order.service;

import com.hust.baseweb.applications.customer.entity.PartyCustomer;
import com.hust.baseweb.applications.logistics.entity.Product;
import com.hust.baseweb.applications.order.entity.OrderItem;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * @author Hien Hoang (hienhoang2702@gmail.com)
 */
public interface RevenueService {
    void updateRevenue(Collection<PartyCustomer> partyCustomers,
                       Map<String, Product> productMap,
                       List<OrderItem> orderItems,
                       Function<OrderItem, PartyCustomer> orderItemToCustomerFunction);
}

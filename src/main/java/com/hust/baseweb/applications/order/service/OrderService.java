package com.hust.baseweb.applications.order.service;


import com.hust.baseweb.applications.customer.entity.PartyCustomer;
import com.hust.baseweb.applications.order.entity.OrderHeader;
import com.hust.baseweb.applications.order.model.ModelCreateOrderInput;
import com.hust.baseweb.applications.order.model.OrderDetailView;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public interface OrderService {
    OrderHeader save(ModelCreateOrderInput order);

    Page<OrderHeader> findAll(Pageable page);

    OrderHeader findByOrderId(String orderId);

    OrderDetailView getOrderDetail(String orderId);

    OrderDetailView convertOrderDetail(OrderHeader order);

    PartyCustomer findCustomerById(UUID partyId);
}

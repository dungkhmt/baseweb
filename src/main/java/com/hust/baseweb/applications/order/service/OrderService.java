package com.hust.baseweb.applications.order.service;


import com.hust.baseweb.applications.customer.entity.PartyCustomer;
import com.hust.baseweb.applications.order.entity.OrderHeader;
import com.hust.baseweb.applications.order.model.CreateOrderDistributor2RetailOutletInputModel;
import com.hust.baseweb.applications.order.model.ModelCreateOrderInput;
import com.hust.baseweb.applications.order.model.OrderDetailView;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface OrderService {
    //OrderHeader save(ModelCreateOrderInput order);
    OrderHeader save(CreateOrderDistributor2RetailOutletInputModel order);

    List<OrderHeader> findAll();

    OrderHeader findByOrderId(String orderId);

    OrderDetailView getOrderDetail(String orderId);

    OrderDetailView convertOrderDetail(OrderHeader order);

    PartyCustomer findCustomerById(UUID partyId);
}

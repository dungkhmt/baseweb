package com.hust.baseweb.applications.order.service;



import java.util.UUID;

import com.hust.baseweb.applications.order.entity.OrderHeader;
import com.hust.baseweb.applications.order.model.ModelCreateOrderInput;
import com.hust.baseweb.applications.order.model.OrderDetailView;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public interface OrderService {
    public OrderHeader save(ModelCreateOrderInput order);
    public Page<OrderHeader> findAll(Pageable page);
    public OrderHeader findByOrderId(String orderId);
    public OrderDetailView getOrderDetail(String orderId);
    public OrderDetailView convertOrderDetail(OrderHeader order);
}

package com.hust.baseweb.applications.order.service;

import com.hust.baseweb.applications.order.entity.OrderHeader;
import com.hust.baseweb.applications.order.model.ModelCreateOrderInput;
import org.springframework.stereotype.Service;

@Service
public interface OrderService {
    public OrderHeader save(ModelCreateOrderInput order);
}

package com.hust.baseweb.applications.order.service;

import org.springframework.stereotype.Service;

import com.hust.baseweb.applications.order.entity.OrderHeader;
import com.hust.baseweb.applications.order.model.ModelCreateOrderInput;

@Service
public interface OrderService {
	public OrderHeader save(ModelCreateOrderInput order);
}

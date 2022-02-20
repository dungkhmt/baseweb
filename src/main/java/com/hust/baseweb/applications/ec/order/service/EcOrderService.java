package com.hust.baseweb.applications.ec.order.service;

import com.hust.baseweb.applications.ec.order.model.CreateOrderModel;
import com.hust.baseweb.applications.ec.order.model.GetAllOrdersInputModel;
import com.hust.baseweb.applications.ec.order.model.OrderResult;
import com.hust.baseweb.applications.ec.order.model.UpdateOrderInput;
import com.hust.baseweb.entity.UserLogin;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface EcOrderService {
    void save(UserLogin u, List<CreateOrderModel> input);
    List<OrderResult> getAllOrders(UserLogin userLogin);
    void update(String orderId, UpdateOrderInput input);
}

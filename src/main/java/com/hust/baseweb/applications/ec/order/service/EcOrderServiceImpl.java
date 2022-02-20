package com.hust.baseweb.applications.ec.order.service;

import com.hust.baseweb.applications.ec.order.model.CreateOrderModel;
import com.hust.baseweb.applications.ec.order.model.OrderResult;
import com.hust.baseweb.applications.ec.order.model.UpdateOrderInput;
import com.hust.baseweb.applications.ec.order.repo.EcOrderItemRepo;
import com.hust.baseweb.applications.logistics.repo.ProductRepo;
import com.hust.baseweb.applications.ec.order.repo.EcOrderHeaderRepo;
import com.hust.baseweb.applications.order.entity.OrderHeader;
import com.hust.baseweb.applications.order.entity.OrderItem;
import com.hust.baseweb.applications.order.entity.OrderType;
import com.hust.baseweb.entity.UserLogin;
import com.hust.baseweb.repo.UserLoginRepo;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class EcOrderServiceImpl implements EcOrderService {
    private UserLoginRepo userLoginRepo;
    private EcOrderHeaderRepo ecOrderHeaderRepo;
    private EcOrderItemRepo ecOrderItemRepo;
    private ProductRepo productRepo;

    @Override
    public void save(UserLogin u, List<CreateOrderModel> input) {
       for (CreateOrderModel createOrderModel: input) {
           OrderItem orderItem = new OrderItem();
           OrderHeader orderHeader = new OrderHeader();
           OrderType orderType = new OrderType();
           orderType.setOrderTypeId("SALES_ORDER");

           orderItem.setOrderId(createOrderModel.getOrderId());
           orderItem.setOrderItemSeqId("0");
           orderItem.setUserId(u.getUserLoginId());
           orderItem.setQuantity(createOrderModel.getQuantity());
           orderItem.setProduct(productRepo.findByProductId(createOrderModel.getProductId()));
           orderItem.setStatusId("ORDER_CREATED");
           orderItem.setCreatedStamp(new Date());

           orderHeader.setOrderId(createOrderModel.getOrderId());
           orderHeader.setOrderType(orderType);
           orderHeader.setProductStoreId(createOrderModel.getStoreId());
           orderHeader.setOrderDate(new Date());

           ecOrderHeaderRepo.save(orderHeader);
           ecOrderItemRepo.save(orderItem);

       }
    }

    @Override
    public List<OrderResult> getAllOrders(UserLogin userLogin) {
        List<OrderResult> orderResults = new ArrayList<>();
        List<OrderItem> orderItems = ecOrderItemRepo.findAllByUserId(userLogin.getUserLoginId());

        for (OrderItem item: orderItems) {
            OrderResult orderResult = new OrderResult();
//            OrderHeader orderHeader = orderHeaderRepo.findByOrderId(item.getOrderId());

            orderResult.setOrderId(item.getOrderId());
            orderResult.setProductName(item.getProduct().getProductName());
            orderResult.setQuantity(item.getQuantity());
            orderResult.setStatusId(item.getStatusId());
            orderResults.add(orderResult);
        }

        return orderResults;
    }

    @Override
    public void update(String orderId, UpdateOrderInput input) {
        OrderItem orderItem = ecOrderItemRepo.findByOrderId(orderId);
        orderItem.setStatusId(input.getStatusId());

        ecOrderItemRepo.save(orderItem);
    }
}

package com.hust.baseweb.applications.order.service;


import com.hust.baseweb.applications.logistics.entity.Facility;
import com.hust.baseweb.applications.logistics.entity.Product;
import com.hust.baseweb.applications.logistics.repo.FacilityRepo;
import com.hust.baseweb.applications.logistics.repo.ProductRepo;
import com.hust.baseweb.applications.order.controller.OrderAPIController;
import com.hust.baseweb.applications.order.entity.*;
import com.hust.baseweb.applications.order.model.ModelCreateOrderInput;
import com.hust.baseweb.applications.order.model.ModelCreateOrderInputOrderItem;
import com.hust.baseweb.applications.order.repo.*;
import com.hust.baseweb.entity.UserLogin;
import com.hust.baseweb.repo.UserLoginRepo;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class OrderServiceImpl implements OrderService {
    public static final String module = OrderServiceImpl.class.getName();

    private UserLoginRepo userLoginRepo;
    private OrderRepo orderRepo;
    private OrderItemRepo orderItemRepo;
    private OrderRoleRepo orderRoleRepo;
    private OrderStatusRepo orderStatusRepo;
    private OrderTypeRepo orderTypeRepo;
    private ProductRepo productRepo;
    private FacilityRepo facilityRepo;
    private SalesChannelRepo salesChannelRepo;

    @Override
    @Transactional
    public OrderHeader save(ModelCreateOrderInput orderInput) {


        OrderType orderType = orderTypeRepo.findByOrderTypeId("SALES_ORDER");
        SalesChannel salesChannel = salesChannelRepo.findBySalesChannelId(orderInput.getSalesChannelId());
        String salesmanId = orderInput.getSalesmanId();
        //System.out.println(module + "::save, salesmanId = " + salesmanId);
        UserLogin salesman = userLoginRepo.findByUserLoginId(salesmanId);
        Facility facility = facilityRepo.findByFacilityId(orderInput.getFacilityId());

        UUID uuid = UUID.randomUUID();
        String orderId = uuid.toString();

        //System.out.println(module + "::save, orderId = " + orderId + ", sales channel = " +
        //(salesChannel != null ? salesChannel.getSalesChannelName() : "null") + ", userLogin = " +
        //		(salesman != null ? salesman.getUserLoginId(): "null") + ", facility = " +
        //(facility != null ? facility.getFacilityName() : "null"));

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date orderDate = null;
        try {
            orderDate = formatter.parse(orderInput.getOrderDate());
        } catch (Exception e) {
            e.printStackTrace();
        }

        OrderHeader order = new OrderHeader();
        order.setOrderId(orderId);
        order.setOrderType(orderType);
        order.setSalesChannel(salesChannel);
        order.setFacility(facility);
        order.setOrderDate(orderDate);

        orderRepo.save(order);

        BigDecimal total = new BigDecimal(0);
        // write to table order_item
        int idx = 0;
        for (ModelCreateOrderInputOrderItem modelCreateOrderInputOrderItem : orderInput.getOrderItems()) {
            idx++;
            String orderItemSeqId = "0000" + idx;
            Product product = productRepo.findByProductId(modelCreateOrderInputOrderItem.getProductId());
            OrderItem orderItem = new OrderItem();
            orderItem.setOrderId(order.getOrderId());
            orderItem.setOrderItemSeqId(orderItemSeqId);
            orderItem.setProduct(product);
            orderItem.setQuantity(modelCreateOrderInputOrderItem.getQuantity());
            orderItem.setUnitPrice(modelCreateOrderInputOrderItem.getUnitPrice());// TOBE FIXED

            orderItemRepo.save(orderItem);
            //System.out.println(module + "::save, order-item " + product.getProductId() + ", price = " + oi.getTotalItemPrice() + ", total = " + total);

            total = total.add(modelCreateOrderInputOrderItem.getTotalItemPrice());
        }

        // write to order_role
        OrderRole orderRole = new OrderRole();
        orderRole.setOrderId(order.getOrderId());
        orderRole.setPartyId(orderInput.getPartyCustomerId());
        orderRole.setRoleTypeId("BILL_TO_CUSTOMER");
        orderRoleRepo.save(orderRole);

        orderRole = new OrderRole();
        orderRole.setOrderId(order.getOrderId());
        orderRole.setPartyId(salesman.getParty().getPartyId());
        orderRole.setRoleTypeId("SALES_EXECUTIVE");// salesman who sales the order (revenue of the order is accounted for this salesman)
        orderRoleRepo.save(orderRole);

        // write to order-status
        OrderStatus orderStatus = new OrderStatus();
        String orderStatusId = UUID.randomUUID().toString();
        orderStatus.setOrder(order);
        orderStatus.setOrderStatusId(orderStatusId);
        orderStatus.setStatusId("ORDER_CREATED");
        orderStatusRepo.save(orderStatus);

        //SimpleDateFormat formatterYYYYMMDD = new SimpleDateFormat("yyyy-mm-dd");
        //String dateYYYYMMDD = formatterYYYYMMDD.format(orderDate);
        String[] s = orderInput.getOrderDate().split(" ");// TOBE FIX
        String dateYYYYMMDD = s[0].trim();
        OrderAPIController.revenueOrderCache.addOrderRevenue(dateYYYYMMDD, total);
        return order;
    }

}

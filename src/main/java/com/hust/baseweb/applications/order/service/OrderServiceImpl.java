package com.hust.baseweb.applications.order.service;


import com.hust.baseweb.applications.customer.entity.PartyCustomer;
import com.hust.baseweb.applications.geo.entity.PostalAddress;
import com.hust.baseweb.applications.geo.repo.PostalAddressRepo;
import com.hust.baseweb.applications.logistics.entity.Facility;
import com.hust.baseweb.applications.logistics.entity.Product;
import com.hust.baseweb.applications.logistics.entity.ProductPrice;
import com.hust.baseweb.applications.logistics.repo.FacilityRepo;
import com.hust.baseweb.applications.logistics.repo.ProductPriceRepo;
import com.hust.baseweb.applications.logistics.repo.ProductRepo;
import com.hust.baseweb.applications.logistics.service.ProductPriceService;
import com.hust.baseweb.applications.order.controller.OrderAPIController;
import com.hust.baseweb.applications.order.entity.*;
import com.hust.baseweb.applications.order.model.ModelCreateOrderInput;
import com.hust.baseweb.applications.order.model.ModelCreateOrderInputOrderItem;
import com.hust.baseweb.applications.order.model.OrderDetailView;
import com.hust.baseweb.applications.order.model.OrderItemDetailView;
import com.hust.baseweb.applications.order.repo.*;
import com.hust.baseweb.applications.sales.entity.PartySalesman;
import com.hust.baseweb.applications.sales.repo.PartySalesmanRepo;
import com.hust.baseweb.applications.sales.service.PartySalesmanService;
import com.hust.baseweb.entity.Party;
import com.hust.baseweb.entity.UserLogin;
import com.hust.baseweb.repo.PartyRepo;
import com.hust.baseweb.repo.UserLoginRepo;
import com.hust.baseweb.utils.CommonUtils;
import com.hust.baseweb.utils.Constant;
import com.hust.baseweb.utils.DateTimeUtils;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
@Log4j2
public class OrderServiceImpl implements OrderService {
    public static final String module = OrderServiceImpl.class.getName();

    private UserLoginRepo userLoginRepo;
    private OrderHeaderRepo orderHeaderRepo;
    private OrderItemRepo orderItemRepo;
    private OrderRoleRepo orderRoleRepo;
    private OrderStatusRepo orderStatusRepo;
    private OrderTypeRepo orderTypeRepo;
    private ProductRepo productRepo;
    private FacilityRepo facilityRepo;
    private SalesChannelRepo salesChannelRepo;
    private ProductPriceService productPriceService;
    private PostalAddressRepo postalAddressRepo;
    private OrderHeaderPageRepo orderHeaderPageRepo;
    private PartySalesmanRepo partySalesmanRepo;
    private PartyCustomerRepo partyCustomerRepo;
    private PartyRepo partyRepo;
    private PartySalesmanService partySalesmanService;

    private ProductPriceRepo productPriceRepo;

    private RevenueService revenueService;

    @Override
    @Transactional
    public OrderHeader save(ModelCreateOrderInput orderInput) {


        OrderType orderType = orderTypeRepo.findByOrderTypeId("SALES_ORDER");
        SalesChannel salesChannel = salesChannelRepo.findBySalesChannelId(orderInput.getSalesChannelId());
        String salesmanId = orderInput.getSalesmanId();
        //System.out.println(module + "::save, salesmanId = " + salesmanId);
        UserLogin salesman = userLoginRepo.findByUserLoginId(salesmanId);
        //Party salesman = partyRepo.
        log.info("save, salesmanId = " + salesmanId + ", partyId = "
                + (salesman != null ? salesman.getParty().getPartyId() : "NULL")
                + ", customerId = " + (orderInput.getToCustomerId() != null ? orderInput.getToCustomerId() : " NULL"));

        Facility facility = facilityRepo.findByFacilityId(orderInput.getFacilityId());

        UUID uuid = UUID.randomUUID();
        String orderId = uuid.toString();

        //System.out.println(module + "::save, orderId = " + orderId + ", sales channel = " +
        //(salesChannel != null ? salesChannel.getSalesChannelName() : "null") + ", userLogin = " +
        //		(salesman != null ? salesman.getUserLoginId(): "null") + ", facility = " +
        //(facility != null ? facility.getFacilityName() : "null"));

        Date orderDate;
        try {
            orderDate = Constant.DATE_FORMAT.parse(orderInput.getOrderDate());
        } catch (ParseException e) {
            e.printStackTrace();
            orderDate = new Date();// take current Date
            log.info("save, input orderDate is null -> take current Date() = " + orderDate.toString());
        }

        PostalAddress shipToPostalAddress = postalAddressRepo.findByContactMechId(orderInput.getShipToAddressId());

        Map<String, Product> productMap = buildProductMap(orderInput);
        Map<String, ProductPrice> productPriceMap = buildProductPriceMap(productMap);

        // iterate for computing grand total
        Double totalGrand = 0.0;
        for (ModelCreateOrderInputOrderItem modelCreateOrderInputOrderItem : orderInput.getOrderItems()) {
            //Product product = productRepo.findByProductId(modelCreateOrderInputOrderItem.getProductId());
            ProductPrice productPrice = productPriceMap.get(modelCreateOrderInputOrderItem.getProductId());
            //orderItem.setUnitPrice(modelCreateOrderInputOrderItem.getUnitPrice());// TOBE FIXED
            double tt;
            if (productPrice != null) {
                tt = productPrice.getPrice() * modelCreateOrderInputOrderItem.getQuantity();
            } else {
                tt = 0.0;
            }
            //total = total.add(modelCreateOrderInputOrderItem.getTotalItemPrice());
            totalGrand += tt;
//            System.out.println(module +
//                    "::save, order-item " +
//                    modelCreateOrderInputOrderItem.getProductId() +
//                    ", price = " +
//                    (pp != null ? pp.getPrice() : 0) +
//                    ", total = " +
//                    totalGrand);

        }

        OrderHeader order = new OrderHeader();
        order.setOrderId(orderId);
        order.setOrderType(orderType);
        order.setSalesChannel(salesChannel);
        order.setOrderDate(orderDate);
        if (shipToPostalAddress != null) {
            order.setShipToPostalAddress(shipToPostalAddress);
        }
        order.setGrandTotal(totalGrand);

        order = orderHeaderRepo.save(order);

        List<OrderItem> orderItems = new ArrayList<>();

        // write to table order_item
        int idx = 0;
        for (ModelCreateOrderInputOrderItem modelCreateOrderInputOrderItem : orderInput.getOrderItems()) {
            idx++;
            String orderItemSeqId = CommonUtils.buildSeqId(idx);//"0000" + idx;
            Product product = productMap.get(modelCreateOrderInputOrderItem.getProductId());
            OrderItem orderItem = new OrderItem();
            orderItem.setOrderId(order.getOrderId());
            orderItem.setOrderItemSeqId(orderItemSeqId);
            orderItem.setProduct(product);
            orderItem.setQuantity(modelCreateOrderInputOrderItem.getQuantity());

            ProductPrice productPrice = productPriceMap.get(product.getProductId());
            //orderItem.setUnitPrice(modelCreateOrderInputOrderItem.getUnitPrice());// TOBE FIXED
            //BigDecimal tt = null;

            if (productPrice != null) {
                orderItem.setUnitPrice(productPrice.getPrice());
                //tt = pp.getPrice().multiply(new BigDecimal(orderItem.getQuantity()));
            } else {
                orderItem.setUnitPrice(0.0);
                //tt = new BigDecimal(0);
            }
            orderItems.add(orderItem);
            //System.out.println(module + "::save, order-item " + product.getProductId() + ", price = " + oi.getTotalItemPrice() + ", total = " + total);
            //total = total.add(modelCreateOrderInputOrderItem.getTotalItemPrice());
            //total = total.add(tt);
        }

        orderItemRepo.saveAll(orderItems);

        // update total
        //order.setGrandTotal(total);
        //orderRepo.save(order);


        // write to order_role
        OrderRole orderRole = new OrderRole();
        orderRole.setOrderId(order.getOrderId());
        //orderRole.setPartyId(orderInput.getPartyCustomerId());
        orderRole.setPartyId(orderInput.getToCustomerId());
        orderRole.setRoleTypeId("BILL_TO_CUSTOMER");
        orderRoleRepo.save(orderRole);

        orderRole = new OrderRole();
        orderRole.setOrderId(order.getOrderId());
        if (salesman != null) {
            orderRole.setPartyId(salesman.getParty().getPartyId());
        }
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
        //String[] s = orderInput.getOrderDate().split(" ");// TOBE FIX
        //String dateYYYYMMDD = s[0].trim();
        String dateYYYYMMDD = DateTimeUtils.date2YYYYMMDD(order.getOrderDate());
        log.info("save, orderDateYYYYMMDD = " + dateYYYYMMDD);
        OrderAPIController.revenueOrderCache.addOrderRevenue(dateYYYYMMDD, totalGrand);

        PartyCustomer partyCustomer = partyCustomerRepo.findById(orderInput.getToCustomerId())
                .orElseThrow(NoSuchElementException::new);

        LocalDate orderLocalDate = orderDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        revenueService.updateRevenue(
                orderItems,
                orderItem -> partyCustomer,
                orderItem -> orderLocalDate
        );

        return order;
    }

    @NotNull
    private Map<String, ProductPrice> buildProductPriceMap(Map<String, Product> productMap) {
        return productPriceRepo.findAllByProductInAndThruDateNull(productMap.values())
                .stream()
                .collect(Collectors.toMap(productPrice1 -> productPrice1.getProduct().getProductId(),
                        productPrice1 -> productPrice1));
    }

    @NotNull
    private Map<String, Product> buildProductMap(ModelCreateOrderInput orderInput) {
        return productRepo.findAllByProductIdIn(Arrays.stream(orderInput.getOrderItems())
                .map(ModelCreateOrderInputOrderItem::getProductId).distinct()
                .collect(Collectors.toList()))
                .stream()
                .collect(Collectors.toMap(Product::getProductId, product -> product));
    }

    @Override
    public List<OrderHeader> findAll() {
        return orderHeaderRepo.findAll();
    }

    @Override
    public OrderHeader findByOrderId(String orderId) {
        return orderHeaderRepo.findByOrderId(orderId);
    }

    @Override
    public OrderDetailView getOrderDetail(String orderId) {
        OrderHeader order = orderHeaderRepo.findByOrderId(orderId);
        if (order == null) {
            return null;
        }
        UUID customerId = null;
        UUID vendorId = null;
        UUID salesmanId = null;
        List<OrderRole> orderRoles = orderRoleRepo.findByOrderId(orderId);
        for (OrderRole or : orderRoles) {
            if (or.getRoleTypeId().equals("BILL_TO_CUSTOMER")) {
                customerId = or.getPartyId();
            } else if (or.getRoleTypeId().equals("BILL_FROM_VENDOR")) {
                vendorId = or.getPartyId();
            } else if (or.getRoleTypeId().equals("SALES_EXECUTIVE")) {
                salesmanId = or.getPartyId();
            }
        }
        PartySalesman salesman = partySalesmanRepo.findByPartyId(salesmanId);
        PartyCustomer vendor = partyCustomerRepo.findByPartyId(vendorId);
        PartyCustomer customer = partyCustomerRepo.findByPartyId(customerId);
        Party partySalesman = partyRepo.findByPartyId(salesmanId);
        UserLogin userLogin = partySalesmanService.findUserLoginOfSalesmanId(salesmanId);

        OrderDetailView odv = new OrderDetailView();
        odv.setCustomerId(customerId);
        odv.setOrderId(orderId);
        odv.setVendorId(vendorId);
        if (customer != null) {
            odv.setCustomerName(customer.getCustomerName());
        }
        if (vendor != null) {
            odv.setVendorName(vendor.getCustomerName());
        }
        if (userLogin != null) {
            odv.setSalesmanLoginId(userLogin.getUserLoginId());
        }
        if (salesman != null) {
            odv.setSalesmanName(salesman.getPerson().getLastName() +
                    " " +
                    salesman.getPerson().getMiddleName() +
                    " " +
                    salesman.getPerson().getFirstName());
        }
        OrderItemDetailView[] oidv = new OrderItemDetailView[order.getOrderItems().size()];
        for (int i = 0; i < order.getOrderItems().size(); i++) {
            OrderItem oi = order.getOrderItems().get(i);
            oidv[i] = new OrderItemDetailView();
            oidv[i].setOrderItemId(oi.getOrderItemSeqId());
            if (oi.getProduct() != null) {
                oidv[i].setProductId(oi.getProduct().getProductId());
                oidv[i].setProductName(oi.getProduct().getProductName());
            }
            oidv[i].setQuantity(oi.getQuantity());
            oidv[i].setUnitPrice(oi.getUnitPrice());
            if (oidv[i].getUnitPrice() != null) {
                oidv[i].setTotalItemPrice(oidv[i].getUnitPrice() * oi.getQuantity());
            }

            oidv[i].setUom(oi.getProduct().getUom().getDescription());
        }
        odv.setOrderItems(oidv);
        return odv;
    }

    @Override
    public OrderDetailView convertOrderDetail(OrderHeader order) {
        if (order == null) {
            return null;
        }
        String orderId = order.getOrderId();
        UUID customerId = null;
        UUID vendorId = null;
        UUID salesmanId = null;
        List<OrderRole> orderRoles = orderRoleRepo.findByOrderId(orderId);
        for (OrderRole or : orderRoles) {
            if (or.getRoleTypeId().equals("BILL_TO_CUSTOMER")) {
                customerId = or.getPartyId();
            } else if (or.getRoleTypeId().equals("BILL_FROM_VENDOR")) {
                vendorId = or.getPartyId();
            } else if (or.getRoleTypeId().equals("SALES_EXECUTIVE")) {
                salesmanId = or.getPartyId();
            }
        }
        PartySalesman salesman = partySalesmanRepo.findByPartyId(salesmanId);
        PartyCustomer vendor = partyCustomerRepo.findByPartyId(vendorId);
        PartyCustomer customer = partyCustomerRepo.findByPartyId(customerId);
        Party partySalesman = partyRepo.findByPartyId(salesmanId);
        UserLogin userLogin = partySalesmanService.findUserLoginOfSalesmanId(salesmanId);

        OrderDetailView odv = new OrderDetailView();
        odv.setCustomerId(customerId);
        odv.setOrderId(orderId);
        odv.setVendorId(vendorId);
        odv.setTotal(order.getGrandTotal());
        odv.setOrderDate(order.getOrderDate());
        if (customer != null) {
            odv.setCustomerName(customer.getCustomerName());
        }
        if (vendor != null) {
            odv.setVendorName(vendor.getCustomerName());
        }
        if (userLogin != null) {
            odv.setSalesmanLoginId(userLogin.getUserLoginId());
        }
        if (salesman != null) {
            odv.setSalesmanName(salesman.getPerson().getLastName() +
                    " " +
                    salesman.getPerson().getMiddleName() +
                    " " +
                    salesman.getPerson().getFirstName());
        }
        OrderItemDetailView[] oidv = new OrderItemDetailView[order.getOrderItems().size()];
        for (int i = 0; i < order.getOrderItems().size(); i++) {
            OrderItem oi = order.getOrderItems().get(i);
            oidv[i] = new OrderItemDetailView();
            oidv[i].setOrderItemId(oi.getOrderItemSeqId());
            if (oi.getProduct() != null) {
                oidv[i].setProductId(oi.getProduct().getProductId());
                oidv[i].setProductName(oi.getProduct().getProductName());
            }
            oidv[i].setQuantity(oi.getQuantity());
            oidv[i].setUnitPrice(oi.getUnitPrice());
            oidv[i].setTotalItemPrice(oidv[i].getUnitPrice() * oi.getQuantity());
            oidv[i].setUom(oi.getProduct().getUom().getDescription());
        }
        odv.setOrderItems(oidv);
        return odv;
    }

    @Override
    public PartyCustomer findCustomerById(UUID partyId) {
        return partyCustomerRepo.findByPartyId(partyId);
    }

}

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
import com.hust.baseweb.applications.order.model.CreateOrderDistributor2RetailOutletInputModel;
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
import com.hust.baseweb.utils.DateTimeUtils;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
@Log4j2
@Transactional
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
    private OrderHeaderSequenceIdRepo orderHeaderSequenceIdRepo;
    private PartySalesmanRepo partySalesmanRepo;
    private PartyCustomerRepo partyCustomerRepo;
    private PartyRepo partyRepo;
    private PartySalesmanService partySalesmanService;

    private ProductPriceRepo productPriceRepo;

    private RevenueService revenueService;


    @Override
    @Transactional
    //public OrderHeader save(ModelCreateOrderInput orderInput) {
    public OrderHeader save(CreateOrderDistributor2RetailOutletInputModel orderInput) {


        OrderType orderType = orderTypeRepo.findByOrderTypeId("SALES_ORDER");
        SalesChannel salesChannel = salesChannelRepo.findBySalesChannelId(orderInput.getSalesChannelId());
        String salesmanId = orderInput.getSalesmanId();
        //System.out.println(module + "::save, salesmanId = " + salesmanId);
        UserLogin userLoginSalesman = userLoginRepo.findByUserLoginId(salesmanId);

        //Party salesman = partyRepo.
        log.info("save, salesmanId = " + salesmanId + ", partyId = "
                + (userLoginSalesman != null ? userLoginSalesman.getParty().getPartyId() : "NULL")
                + ", customerId = " + (orderInput.getToCustomerId() != null ? orderInput.getToCustomerId() : " NULL"));

        Facility facility = facilityRepo.findByFacilityId(orderInput.getFacilityId());

        OrderHeaderSequenceId id = orderHeaderSequenceIdRepo.save(new OrderHeaderSequenceId());
        String orderId = OrderHeader.convertSequenceIdToOrderId(id.getId());

        //System.out.println(module + "::save, orderId = " + orderId + ", sales channel = " +
        //(salesChannel != null ? salesChannel.getSalesChannelName() : "null") + ", userLogin = " +
        //		(salesman != null ? salesman.getUserLoginId(): "null") + ", facility = " +
        //(facility != null ? facility.getFacilityName() : "null"));

        Date orderDate = new Date();
        /*
        try {
            orderDate = Constant.DATE_FORMAT.parse(orderInput.getOrderDate());
        } catch (ParseException e) {
            //e.printStackTrace();
            orderDate = new Date();// take current Date
            log.info("save, exception input orderDate is null -> take current Date() = " + orderDate.toString());
        }
        */


        PostalAddress shipToPostalAddress = postalAddressRepo.findByContactMechId(orderInput.getShipToAddressId());

        Map<String, Product> productMap = buildProductMap(orderInput);
        Map<String, ProductPrice> productPriceMap = buildProductPriceMap(productMap);

        // iterate for computing grand total
        double totalGrand = 0.0;
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

        //PartyCustomer partyCustomer = partyCustomerRepo.findById(orderInput.getToCustomerId())
        //        .orElseThrow(NoSuchElementException::new);

        Party customer = partyRepo.findByPartyId(orderInput.getToCustomerId());
        Party salesman = userLoginSalesman.getParty();
        Party distributor = partyRepo.findByPartyId(orderInput.getFromVendorId());
        //order.setPartyCustomer(partyCustomer);
        order.setPartyCustomer(customer);
        order.setPartyVendor(distributor);
        order.setPartySalesman(salesman);

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
            orderRole.setPartyId(salesman.getPartyId());
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


        LocalDate orderLocalDate = orderDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        revenueService.updateRevenue(
                orderItems,
                //orderItem -> partyCustomer,
                orderItem -> customer,
                orderItem -> orderLocalDate
        );

        return order;
    }

    @NotNull
    private Map<String, ProductPrice> buildProductPriceMap(Map<String, Product> productMap) {
        Date now = new Date();
        return productPriceRepo.findAllByProductInAndThruDateNullOrThruDateAfter(productMap.values(), now)
                .stream()
                .collect(Collectors.toMap(productPrice -> productPrice.getProduct().getProductId(),
                        productPrice -> productPrice));
    }

    @NotNull
    //private Map<String, Product> buildProductMap(ModelCreateOrderInput orderInput) {
    private Map<String, Product> buildProductMap(CreateOrderDistributor2RetailOutletInputModel orderInput) {
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
        for (OrderRole orderRole : orderRoles) {
            if (orderRole.getRoleTypeId().equals("BILL_TO_CUSTOMER")) {
                customerId = orderRole.getPartyId();
            } else if (orderRole.getRoleTypeId().equals("BILL_FROM_VENDOR")) {
                vendorId = orderRole.getPartyId();
            } else if (orderRole.getRoleTypeId().equals("SALES_EXECUTIVE")) {
                salesmanId = orderRole.getPartyId();
            }
        }
        PartySalesman salesman = partySalesmanRepo.findByPartyId(salesmanId);
        PartyCustomer vendor = partyCustomerRepo.findByPartyId(vendorId);
        PartyCustomer customer = partyCustomerRepo.findByPartyId(customerId);
        Party partySalesman = partyRepo.findByPartyId(salesmanId);
        UserLogin userLogin = partySalesmanService.findUserLoginOfSalesmanId(salesmanId);

        OrderDetailView orderDetailView = new OrderDetailView();
        orderDetailView.setOrderDate(order.getOrderDate());
        orderDetailView.setCustomerId(customerId);
        orderDetailView.setOrderId(orderId);
        orderDetailView.setVendorId(vendorId);
        if (customer != null) {
            orderDetailView.setCustomerName(customer.getCustomerName());
        }
        if (vendor != null) {
            orderDetailView.setVendorName(vendor.getCustomerName());
        }
        if (userLogin != null) {
            orderDetailView.setSalesmanLoginId(userLogin.getUserLoginId());
        }
        if (salesman != null) {
            orderDetailView.setSalesmanName(salesman.getPerson().getLastName() +
                    " " +
                    salesman.getPerson().getMiddleName() +
                    " " +
                    salesman.getPerson().getFirstName());
        }
        OrderItemDetailView[] orderItemDetailViews = new OrderItemDetailView[order.getOrderItems().size()];
        for (int i = 0; i < order.getOrderItems().size(); i++) {
            OrderItem orderItem = order.getOrderItems().get(i);
            orderItemDetailViews[i] = new OrderItemDetailView();
            orderItemDetailViews[i].setOrderItemId(orderItem.getOrderItemSeqId());
            if (orderItem.getProduct() != null) {
                orderItemDetailViews[i].setProductId(orderItem.getProduct().getProductId());
                orderItemDetailViews[i].setProductName(orderItem.getProduct().getProductName());
            }
            orderItemDetailViews[i].setQuantity(orderItem.getQuantity());
            orderItemDetailViews[i].setUnitPrice(orderItem.getUnitPrice());
            if (orderItemDetailViews[i].getUnitPrice() != null) {
                orderItemDetailViews[i].setTotalItemPrice(orderItemDetailViews[i].getUnitPrice() *
                        orderItem.getQuantity());
            }

            orderItemDetailViews[i].setUom(orderItem.getProduct().getUom().getDescription());
        }
        orderDetailView.setOrderItems(orderItemDetailViews);
        return orderDetailView;
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
        for (OrderRole orderRole : orderRoles) {
            if (orderRole.getRoleTypeId().equals("BILL_TO_CUSTOMER")) {
                customerId = orderRole.getPartyId();
            } else if (orderRole.getRoleTypeId().equals("BILL_FROM_VENDOR")) {
                vendorId = orderRole.getPartyId();
            } else if (orderRole.getRoleTypeId().equals("SALES_EXECUTIVE")) {
                salesmanId = orderRole.getPartyId();
            }
        }
        PartySalesman salesman = partySalesmanRepo.findByPartyId(salesmanId);
        PartyCustomer vendor = partyCustomerRepo.findByPartyId(vendorId);
        PartyCustomer customer = partyCustomerRepo.findByPartyId(customerId);
        Party partySalesman = partyRepo.findByPartyId(salesmanId);
        UserLogin userLogin = partySalesmanService.findUserLoginOfSalesmanId(salesmanId);

        OrderDetailView orderDetailView = new OrderDetailView();
        orderDetailView.setCustomerId(customerId);
        orderDetailView.setOrderId(orderId);
        orderDetailView.setVendorId(vendorId);
        orderDetailView.setTotal(order.getGrandTotal());
        orderDetailView.setOrderDate(order.getOrderDate());
        if (customer != null) {
            orderDetailView.setCustomerName(customer.getCustomerName());
        }
        if (vendor != null) {
            orderDetailView.setVendorName(vendor.getCustomerName());
        }
        if (userLogin != null) {
            orderDetailView.setSalesmanLoginId(userLogin.getUserLoginId());
        }
        if (salesman != null) {
            orderDetailView.setSalesmanName(salesman.getPerson().getLastName() +
                    " " +
                    salesman.getPerson().getMiddleName() +
                    " " +
                    salesman.getPerson().getFirstName());
        }
        OrderItemDetailView[] orderItemDetailViews = new OrderItemDetailView[order.getOrderItems().size()];
        for (int i = 0; i < order.getOrderItems().size(); i++) {
            OrderItem orderItem = order.getOrderItems().get(i);
            orderItemDetailViews[i] = new OrderItemDetailView();
            orderItemDetailViews[i].setOrderItemId(orderItem.getOrderItemSeqId());
            if (orderItem.getProduct() != null) {
                orderItemDetailViews[i].setProductId(orderItem.getProduct().getProductId());
                orderItemDetailViews[i].setProductName(orderItem.getProduct().getProductName());
            }
            orderItemDetailViews[i].setQuantity(orderItem.getQuantity());
            orderItemDetailViews[i].setUnitPrice(orderItem.getUnitPrice());
            orderItemDetailViews[i].setTotalItemPrice(orderItemDetailViews[i].getUnitPrice() * orderItem.getQuantity());
            orderItemDetailViews[i].setUom(orderItem.getProduct().getUom().getDescription());
        }
        orderDetailView.setOrderItems(orderItemDetailViews);
        return orderDetailView;
    }

    @Override
    public PartyCustomer findCustomerById(UUID partyId) {
        return partyCustomerRepo.findByPartyId(partyId);
    }

}

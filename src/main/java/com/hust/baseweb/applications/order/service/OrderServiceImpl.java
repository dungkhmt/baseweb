package com.hust.baseweb.applications.order.service;


import com.hust.baseweb.applications.accounting.document.*;
import com.hust.baseweb.applications.accounting.repo.*;
import com.hust.baseweb.applications.customer.entity.PartyCustomer;
import com.hust.baseweb.applications.geo.entity.PostalAddress;
import com.hust.baseweb.applications.geo.repo.PostalAddressRepo;
import com.hust.baseweb.applications.logistics.entity.*;
import com.hust.baseweb.applications.logistics.repo.*;
import com.hust.baseweb.applications.logistics.service.ProductPriceService;
import com.hust.baseweb.applications.order.controller.OrderAPIController;
import com.hust.baseweb.applications.order.document.OrderHeaderRemoved;
import com.hust.baseweb.applications.order.document.aggregation.RevenueUpdateType;
import com.hust.baseweb.applications.order.entity.*;
import com.hust.baseweb.applications.order.model.*;
import com.hust.baseweb.applications.order.repo.*;
import com.hust.baseweb.applications.order.repo.mongodb.OrderHeaderRemovedRepo;
import com.hust.baseweb.applications.sales.entity.PartySalesman;
import com.hust.baseweb.applications.sales.repo.PartySalesmanRepo;
import com.hust.baseweb.applications.sales.service.PartySalesmanService;
import com.hust.baseweb.applications.tms.entity.DeliveryTripDetail;
import com.hust.baseweb.applications.tms.entity.Shipment;
import com.hust.baseweb.applications.tms.entity.ShipmentItem;
import com.hust.baseweb.applications.tms.repo.DeliveryTripDetailRepo;
import com.hust.baseweb.applications.tms.repo.ShipmentItemRepo;
import com.hust.baseweb.applications.tms.repo.ShipmentRepo;
import com.hust.baseweb.applications.tms.repo.status.DeliveryTripDetailStatusRepo;
import com.hust.baseweb.applications.tms.repo.status.ShipmentItemStatusRepo;
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
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.Serializable;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
@Log4j2
@Transactional
@org.springframework.transaction.annotation.Transactional
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

    private SupplierRepo supplierRepo;
    private ProductPriceSupplierRepo productPriceSupplierRepo;

    private OrderHeaderRemovedRepo orderHeaderRemovedRepo;

    private ShipmentRepo shipmentRepo;
    private ShipmentItemRepo shipmentItemRepo;

    private ShipmentItemStatusRepo shipmentItemStatusRepo;
    private ShipmentItemRoleRepo shipmentItemRoleRepo;
    private DeliveryTripDetailRepo deliveryTripDetailRepo;

    private InventoryItemDetailRepo inventoryItemDetailRepo;

    private DeliveryTripDetailStatusRepo deliveryTripDetailStatusRepo;

    private PaymentApplicationRepo paymentApplicationRepo;

    private OrderItemBillingRepo orderItemBillingRepo;

    private InvoiceItemRepo invoiceItemRepo;
    private InvoiceRepo invoiceRepo;

    private PaymentRepo paymentRepo;

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
        Party salesman = Optional.ofNullable(userLoginSalesman).map(UserLogin::getParty).orElse(null);
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
            orderItem -> orderLocalDate,
            RevenueUpdateType.INCREASE
        );

        return order;
    }

    @NotNull
    private Map<String, ProductPrice> buildProductPriceMap(Map<String, Product> productMap) {
        Date now = new Date();
        return productPriceRepo
            .findAllByProductInAndThruDateNullOrThruDateAfter(productMap.values(), now)
            .stream()
            .collect(Collectors.toMap(
                productPrice -> productPrice.getProduct().getProductId(),
                productPrice -> productPrice));
    }

    @NotNull
    //private Map<String, Product> buildProductMap(ModelCreateOrderInput orderInput) {
    private Map<String, Product> buildProductMap(CreateOrderDistributor2RetailOutletInputModel orderInput) {
        return productRepo
            .findAllByProductIdIn(Arrays
                                      .stream(orderInput.getOrderItems())
                                      .map(ModelCreateOrderInputOrderItem::getProductId)
                                      .distinct()
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

        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STANDARD).setAmbiguityIgnored(true);
        modelMapper
            .typeMap(ShipmentItem.class, ShipmentItemDetailView.class)
            .addMapping(
                shipmentItem -> shipmentItem.getShipment().getShipmentId(),
                ShipmentItemDetailView::setShipmentId);

        setShipment(orderId, orderDetailView, modelMapper);

        List<String> invoiceIds = setInvoice(orderId, orderDetailView, modelMapper);

        setPayment(orderDetailView, modelMapper, invoiceIds);


        return orderDetailView;
    }

    private void setShipment(String orderId, OrderDetailView orderDetailView, ModelMapper modelMapper) {
        List<ShipmentItem> shipmentItems = shipmentItemRepo.findAllByOrderId(orderId);
        List<Shipment> shipments = shipmentItems
            .stream()
            .map(ShipmentItem::getShipment)
            .distinct()
            .collect(Collectors.toList());

        orderDetailView.setShipmentDetailViews(shipments
                                                   .stream()
                                                   .map(shipment -> modelMapper.map(shipment, ShipmentDetailView.class))
                                                   .collect(Collectors.toList()));
        orderDetailView.setShipmentItemDetailViews(shipmentItems
                                                       .stream()
                                                       .map(shipmentItem -> modelMapper.map(
                                                           shipmentItem,
                                                           ShipmentItemDetailView.class))
                                                       .collect(Collectors.toList()));
    }

    @NotNull
    private List<String> setInvoice(String orderId, OrderDetailView orderDetailView, ModelMapper modelMapper) {
        List<OrderItemBilling> orderItemBillings = orderItemBillingRepo.findAllById_OrderId(orderId);
        List<InvoiceItem.Id> invoiceItemIds = orderItemBillings
            .stream()
            .map(orderItemBilling -> orderItemBilling.getId().toInvoiceItemId())
            .distinct()
            .collect(Collectors.toList());
        List<InvoiceItem> invoiceItems = invoiceItemRepo.findAllByIdIn(invoiceItemIds);
        List<String> invoiceIds = invoiceItemIds
            .stream()
            .map(InvoiceItem.Id::getInvoiceId)
            .distinct()
            .collect(Collectors.toList());

        List<Invoice> invoices = invoiceRepo.findAllByInvoiceIdIn(invoiceIds);

        orderDetailView.setInvoiceDetailViews(invoices
                                                  .stream()
                                                  .map(invoice -> modelMapper.map(invoice, InvoiceDetailView.class))
                                                  .collect(Collectors.toList()));
        orderDetailView.setInvoiceItemDetailViews(invoiceItems
                                                      .stream()
                                                      .map(invoiceItem -> modelMapper.map(
                                                          invoiceItem,
                                                          InvoiceItemDetailView.class))
                                                      .collect(Collectors.toList()));
        return invoiceIds;
    }

    private void setPayment(OrderDetailView orderDetailView, ModelMapper modelMapper, List<String> invoiceIds) {
        List<PaymentApplication> paymentApplications = paymentApplicationRepo.findAllByInvoiceIdIn(invoiceIds);
        List<String> paymentIds = paymentApplications
            .stream()
            .map(PaymentApplication::getPaymentId)
            .collect(Collectors.toList());
        List<Payment> payments = paymentRepo.findAllByPaymentIdIn(paymentIds);

        orderDetailView.setPaymentDetailViews(payments
                                                  .stream()
                                                  .map(payment -> modelMapper.map(payment, PaymentDetailView.class))
                                                  .collect(Collectors.toList()));
        orderDetailView.setPaymentApplicationDetailViews(paymentApplications
                                                             .stream()
                                                             .map(paymentApplication -> modelMapper.map(
                                                                 paymentApplication,
                                                                 PaymentApplicationDetailView.class))
                                                             .collect(Collectors.toList()));
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

    @Override
    public List<OrderHeader.PurchaseModel> getAllPurchaseOrder() {
        // lấy danh sách order có type PURCHASE_ORDER
        OrderType purchaseOrder = orderTypeRepo.findById("PURCHASE_ORDER").orElseThrow(NoSuchElementException::new);
        List<OrderHeader> orderHeaders = orderHeaderRepo.findAllByOrderType(purchaseOrder);
        Map<String, OrderHeader> orderHeaderMap = orderHeaders
            .stream()
            .collect(Collectors.toMap(OrderHeader::getOrderId, o -> o));

        // lấy các nhà cung cấp trong các order và map nó theo uuid
        List<UUID> vendorIds = orderHeaders
            .stream()
            .map(orderHeader -> orderHeader.getPartyVendor().getPartyId())
            .distinct()
            .collect(Collectors.toList());
        Map<UUID, Supplier> supplierMap = supplierRepo
            .findAllByPartyIdIn(vendorIds)
            .stream()
            .collect(Collectors.toMap(Supplier::getPartyId, s -> s));

        // các order item của các order
        List<OrderItem> orderItems = orderItemRepo.findAllByOrderIdIn(new ArrayList<>(orderHeaderMap.keySet()));

        // các product của các order
        List<Product> products = orderItems.stream().map(OrderItem::getProduct).collect(Collectors.toList());

        // dựng map về giá sản phẩm từ nhà cung cấp, key = product+supplier
        Date now = new Date();
        Map<List<? extends Serializable>, ProductPriceSupplier> productPriceSupplierMap =
            productPriceSupplierRepo
                .findAllByPartySupplierInAndProductInAndThruDateNullOrThruDateAfter(
                    supplierMap.values(), products, now)
                .stream()
                .collect(Collectors.toMap(productPriceSupplier -> Arrays.asList(
                    productPriceSupplier
                        .getProduct()
                        .getProductId(),
                    productPriceSupplier
                        .getPartySupplier()
                        .getPartyId()), p -> p));


        // dựng map order id --> thành tiền
        Map<String, Double> orderIdToTotalAmount = orderItems.stream().collect(Collectors.groupingBy(
            OrderItem::getOrderId,
            Collectors.summingDouble(orderItem -> {
                UUID supplierPartyId = orderHeaderMap.get(orderItem.getOrderId()).getPartyVendor().getPartyId();
                String productId = orderItem.getProduct().getProductId();
                ProductPriceSupplier productPriceSupplier = productPriceSupplierMap.get(Arrays.asList(
                    productId,
                    supplierPartyId));
                return Optional.ofNullable(productPriceSupplier).map(ProductPriceSupplier::getUnitPrice).orElse(0) *
                       orderItem.getQuantity();
            })));

        return orderHeaders.stream().map(orderHeader -> orderHeader.toPurchaseModel(
            Optional
                .ofNullable(supplierMap.get(orderHeader.getPartyVendor().getPartyId()))
                .map(Supplier::getSupplierName)
                .orElse(null),
            orderIdToTotalAmount.get(orderHeader.getOrderId())
        )).collect(Collectors.toList());
    }

    @Override
    public boolean createPurchaseOrder(OrderHeader.PurchaseCreateModel purchaseCreateModel) {
        OrderHeaderSequenceId id = orderHeaderSequenceIdRepo.save(new OrderHeaderSequenceId());
        String orderId = OrderHeader.convertSequenceIdToOrderId(id.getId());

        OrderType purchaseOrder = orderTypeRepo.findById("PURCHASE_ORDER").orElseThrow(NoSuchElementException::new);
        Date now = new Date();

        Party vendorParty = partyRepo
            .findById(UUID.fromString(purchaseCreateModel.getSupplierPartyId()))
            .orElseThrow(NoSuchElementException::new);

        OrderHeader orderHeader = new OrderHeader(orderId, purchaseOrder, null, now, 0.0, null,
                                                  false, now, now, null, vendorParty, null, null, null, null, null);

        orderHeader = orderHeaderRepo.save(orderHeader);

        List<String> productIds = purchaseCreateModel
            .getProductQuantities()
            .stream()
            .map(OrderHeader.PurchaseCreateModel.ProductQuantity::getProductId)
            .distinct()
            .collect(Collectors.toList());
        Map<String, Product> productMap = productRepo
            .findAllByProductIdIn(productIds)
            .stream()
            .collect(Collectors.toMap(Product::getProductId, p -> p));

        Supplier supplier = supplierRepo
            .findById(UUID.fromString(purchaseCreateModel.getSupplierPartyId()))
            .orElseThrow(NoSuchElementException::new);
        Map<String, Integer> productIdToSupplierUnitPrice = productPriceSupplierRepo
            .findAllByPartySupplierAndThruDateNullOrThruDateAfter(
                supplier,
                now)
            .stream()
            .collect(Collectors.toMap(
                productPriceSupplier -> productPriceSupplier.getProduct().getProductId(),
                ProductPriceSupplier::getUnitPrice));

        List<OrderItem> orderItems = new ArrayList<>();

        for (int i = 0; i < purchaseCreateModel.getProductQuantities().size(); i++) {
            OrderHeader.PurchaseCreateModel.ProductQuantity productQuantity =
                purchaseCreateModel.getProductQuantities().get(i);
            String productId = productQuantity.getProductId();
            OrderItem orderItem = new OrderItem(
                orderId,
                i + "",
                productMap.get(productId),
                Optional
                    .ofNullable(productIdToSupplierUnitPrice.get(productId))
                    .map(unitPrice -> 1.0 * unitPrice)
                    .orElse(null),
                productQuantity.getQuantity(),
                0);
            orderItems.add(orderItem);
        }

        orderItemRepo.saveAll(orderItems);

        return true;
    }

    @Override
    public boolean deleteOrders(OrderHeader.DeleteModel deleteModel) {
        try {
            List<OrderHeader> orderHeaders = orderHeaderRepo.findAllByOrderDateBetween(
                Constant.DATE_FORMAT.parse(deleteModel.getFromDate()),
                Constant.DATE_FORMAT.parse(deleteModel.getToDate()));

            Map<String, OrderHeader> orderHeaderMap = orderHeaders
                .stream()
                .collect(Collectors.toMap(OrderHeader::getOrderId, o -> o));

            List<String> orderIds = new ArrayList<>(orderHeaderMap.keySet());

            orderRoleRepo.deleteAllByOrderIdIn(orderIds);
            orderRoleRepo.flush();

            Map<String, List<OrderStatus>> orderIdToOrderStatuses = orderStatusRepo
                .findAllByOrderIn(orderHeaders)
                .stream()
                .collect(Collectors.groupingBy(orderStatus -> orderStatus.getOrder().getOrderId()));
            for (OrderHeader orderHeader : orderHeaders) {
                orderHeader.setOrderStatuses(orderIdToOrderStatuses.get(orderHeader.getOrderId()));
            }

            ModelMapper modelMapper = new ModelMapper();
            modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STANDARD);

            List<OrderHeaderRemoved> orderHeadersRemoved = orderHeaders
                .stream()
                .map(orderHeader -> modelMapper.map(orderHeader, OrderHeaderRemoved.class))
                .collect(Collectors.toList());

            orderStatusRepo.deleteAllByOrderIn(orderHeaders);
            orderStatusRepo.flush();


            List<OrderItem> orderItems = orderItemRepo.findAllByOrderIdIn(orderIds);

            List<ShipmentItem> shipmentItems = shipmentItemRepo.findAllByOrderIdIn(orderIds);

            shipmentItemStatusRepo.deleteAllByShipmentItemIn(shipmentItems);
            shipmentItemStatusRepo.flush();
//
            shipmentItemRoleRepo.deleteAllByShipmentItemIn(shipmentItems);
            shipmentItemRoleRepo.flush();

            List<DeliveryTripDetail> deliveryTripDetails = deliveryTripDetailRepo.findAllByShipmentItemIn(shipmentItems);

            deliveryTripDetailStatusRepo.deleteAllByDeliveryTripDetailIn(deliveryTripDetails);
            deliveryTripDetailStatusRepo.flush();

            // exception PSQLException: ERROR: update or delete on table "shipment_item" violates foreign key
            //  constraint "fk_shipment_item_status_shipment_item_id" on table "shipment_item_status" --> fixed
            shipmentItemRepo.deleteInBatch(shipmentItems);
            shipmentItemRepo.flush();

            inventoryItemDetailRepo.deleteAllByOrderIdIn(orderIds);
            inventoryItemDetailRepo.flush();

            orderItemRepo.deleteAllByOrderIdIn(orderIds);
            orderItemRepo.flush();

            orderHeaderRepo.deleteAllByOrderIdIn(orderIds);
            orderHeaderRepo.flush();

            log.info("Deleted {} order headers", orderIds.size());

            orderHeaderRemovedRepo.saveAll(orderHeadersRemoved);

            // update revenue
            revenueService.updateRevenue(
                orderItems,
                orderItem -> orderHeaderMap.get(orderItem.getOrderId()).getPartyCustomer(),
                orderItem -> orderHeaderMap
                    .get(orderItem.getOrderId())
                    .getOrderDate()
                    .toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate(),
                RevenueUpdateType.DECREASE
            );

            return true;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

}

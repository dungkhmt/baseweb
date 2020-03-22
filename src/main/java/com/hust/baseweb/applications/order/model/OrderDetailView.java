package com.hust.baseweb.applications.order.model;

import com.hust.baseweb.applications.customer.entity.PartyCustomer;
import com.hust.baseweb.applications.order.entity.OrderHeader;
import com.hust.baseweb.applications.order.entity.OrderRole;
import com.hust.baseweb.applications.order.service.OrderService;
import com.hust.baseweb.applications.sales.service.PartySalesmanService;
import com.hust.baseweb.entity.Person;
import com.hust.baseweb.entity.UserLogin;
import com.hust.baseweb.rest.user.DPerson;
import com.hust.baseweb.service.UserService;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.UUID;

@Getter
@Setter
public class OrderDetailView {
    private String orderId;
    private Date orderDate;
    private UUID customerId;
    private String customerName;
    private UUID vendorId;
    private String vendorName;
    private String salesmanLoginId;
    private String salesmanName;
    private Double total;
    private OrderItemDetailView[] orderItems;

    public OrderDetailView(OrderHeader order, OrderService orderService, PartySalesmanService salesmanService, UserService userService) {
        // TODO: to be improved

        orderId = order.getOrderId();
        orderDate = order.getOrderDate();
        total = order.getGrandTotal();

        PartyCustomer customer = null;
        PartyCustomer vendor = null;
        UserLogin salesman = null;
        DPerson salesmanPerson = null;
        for (OrderRole or : order.getOrderRoles()) {
            if (or.getRoleTypeId().equals("BILL_FROM_VENDOR")) {
                vendor = orderService.findCustomerById(or.getPartyId());
            } else if (or.getRoleTypeId().equals("BILL_TO_CUSTOMER")) {
                customer = orderService.findCustomerById(or.getPartyId());
            } else if (or.getRoleTypeId().equals("SALES_EXECUTIVE")) {
                salesman = salesmanService.findUserLoginOfSalesmanId(or.getPartyId());
                //DPerson p = userService.findByPartyId(partyId);
                salesmanPerson = userService.findByPartyId(or.getPartyId().toString());
            }
        }
        if (customer != null) {
            customerName = customer.getCustomerName();
            customerId = customer.getPartyId();
        }
        if (vendor != null) {
            vendorName = vendor.getCustomerName();
            vendorId = vendor.getPartyId();
        }
        if (salesman != null) {
            salesmanLoginId = salesman.getUserLoginId();
        }
        if (salesmanPerson != null) {
            Person person = salesmanPerson.getPerson();
            if (person != null) {
                salesmanName = person.getLastName() + " " + person.getMiddleName() + " " + person.getFirstName();
            }

        }

    }

    public OrderDetailView() {
        // TODO:
    }
}

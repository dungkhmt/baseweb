package com.hust.baseweb.applications.accounting.repo;

import com.hust.baseweb.applications.accounting.document.OrderItemBilling;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * @author Hien Hoang (hienhoang2702@gmail.com)
 */
public interface OrderItemBillingRepo extends MongoRepository<OrderItemBilling, OrderItemBilling.Id> {

    List<OrderItemBilling> findAllById_InvoiceId(String invoiceId);

    List<OrderItemBilling> findAllById_OrderId(String orderId);
}

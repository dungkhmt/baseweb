package com.hust.baseweb.applications.accounting.service;

import com.hust.baseweb.applications.accounting.document.Invoice;
import com.hust.baseweb.applications.accounting.document.InvoiceItem;
import com.hust.baseweb.applications.accounting.document.OrderItemBilling;
import com.hust.baseweb.applications.accounting.repo.InvoiceItemRepo;
import com.hust.baseweb.applications.accounting.repo.InvoiceRepo;
import com.hust.baseweb.applications.accounting.repo.OrderItemBillingRepo;
import com.hust.baseweb.applications.order.entity.CompositeOrderItemId;
import com.hust.baseweb.applications.order.entity.OrderItem;
import com.hust.baseweb.applications.order.repo.OrderItemRepo;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Hien Hoang (hienhoang2702@gmail.com)
 */
@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
@Transactional
public class InvoiceItemServiceImpl implements InvoiceItemService {

    private InvoiceRepo invoiceRepo;
    private InvoiceItemRepo invoiceItemRepo;
    private OrderItemBillingRepo orderItemBillingRepo;
    private OrderItemRepo orderItemRepo;

    @Override
    public List<InvoiceItem.Model> findByInvoiceId(String invoiceId) {
        Invoice invoice = invoiceRepo.findById(invoiceId).orElseThrow(NoSuchElementException::new);

        List<InvoiceItem> invoiceItems = invoiceItemRepo.findAllById_InvoiceId(invoiceId);
        List<OrderItemBilling> orderItemBillings = orderItemBillingRepo.findAllById_InvoiceId(invoiceId);

        List<String> orderIds = orderItemBillings
            .stream()
            .map(orderItemBilling -> orderItemBilling.getId().getOrderId())
            .distinct()
            .collect(Collectors.toList());
        List<String> orderItemSeqIds = orderItemBillings
            .stream()
            .map(orderItemBilling -> orderItemBilling.getId().getOrderItemSeqId())
            .distinct()
            .collect(Collectors.toList());

        Map<CompositeOrderItemId, OrderItem> orderItemMap = orderItemRepo.findAllByOrderIdInAndOrderItemSeqIdIn(
            orderIds,
            orderItemSeqIds).stream().collect(Collectors.toMap(orderItem -> new CompositeOrderItemId(
            orderItem.getOrderId(),
            orderItem.getOrderItemSeqId()), orderItem -> orderItem));
        Map<InvoiceItem.Id, OrderItem> orderItemBillingToOrderItem = orderItemBillings
            .stream()
            .collect(Collectors.toMap(
                orderItemBilling -> new InvoiceItem.Id(
                    orderItemBilling.getId().getInvoiceId(),
                    orderItemBilling.getId().getInvoiceItemSeqId()),
                orderItemBilling -> orderItemMap.get(new CompositeOrderItemId(
                    orderItemBilling.getId().getOrderId(),
                    orderItemBilling
                        .getId()
                        .getOrderItemSeqId()))));

        return invoiceItems
            .stream()
            .map(invoiceItem -> Optional
                .ofNullable(orderItemBillingToOrderItem.get(invoiceItem.getId()))
                .map(orderItem -> invoiceItem.toModel(invoice, orderItem))
                .orElse(invoiceItem.toModel(invoice, null))) // empty product info
            .collect(Collectors.toList());
    }
}

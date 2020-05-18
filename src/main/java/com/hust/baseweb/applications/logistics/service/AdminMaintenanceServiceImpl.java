package com.hust.baseweb.applications.logistics.service;

import com.hust.baseweb.applications.accounting.repo.*;
import com.hust.baseweb.applications.accounting.repo.sequenceid.InvoiceSequenceIdRepo;
import com.hust.baseweb.applications.accounting.repo.sequenceid.PaymentSequenceIdRepo;
import com.hust.baseweb.applications.logistics.repo.*;
import com.hust.baseweb.applications.order.repo.OrderHeaderRepo;
import com.hust.baseweb.applications.order.repo.OrderHeaderSequenceIdRepo;
import com.hust.baseweb.applications.order.repo.OrderItemRepo;
import com.hust.baseweb.applications.tms.repo.*;
import com.hust.baseweb.applications.tms.repo.status.DeliveryTripDetailStatusRepo;
import com.hust.baseweb.applications.tms.repo.status.DeliveryTripStatusRepo;
import com.hust.baseweb.applications.tms.repo.status.ShipmentItemStatusRepo;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Hien Hoang (hienhoang2702@gmail.com)
 */
@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
@Transactional
@javax.transaction.Transactional
public class AdminMaintenanceServiceImpl implements AdminMaintenanceService {

    private InventoryItemDetailRepo inventoryItemDetailRepo;
    private InventoryItemRepo inventoryItemRepo;
    private ShipmentItemStatusRepo shipmentItemStatusRepo;
    private OrderItemBillingRepo orderItemBillingRepo;
    private OrderItemRepo orderItemRepo;
    private InvoiceItemRepo invoiceItemRepo;
    private InvoiceStatusRepo invoiceStatusRepo;
    private PaymentApplicationRepo paymentApplicationRepo;
    private InvoiceRepo invoiceRepo;
    private PaymentRepo paymentRepo;
    private InvoiceSequenceIdRepo invoiceSequenceIdRepo;
    private PaymentSequenceIdRepo paymentSequenceIdRepo;
    private ShipmentItemDeliveryPlanRepo shipmentItemDeliveryPlanRepo;
    private DeliveryTripDetailStatusRepo deliveryTripDetailStatusRepo;
    private DeliveryTripDetailRepo deliveryTripDetailRepo;
    private ShipmentItemRepo shipmentItemRepo;
    private OrderHeaderRepo orderHeaderRepo;
    private ShipmentRepo shipmentRepo;
    private DeliveryTripStatusRepo deliveryTripStatusRepo;
    private DeliveryTripRepo deliveryTripRepo;
    private DeliveryPlanRepo deliveryPlanRepo;
    private OrderHeaderSequenceIdRepo orderHeaderSequenceIdRepo;
    private ReceiptItemRepo receiptItemRepo;
    private ReceiptRepo receiptRepo;
    private ReceiptSequenceIdRepo receiptSequenceIdRepo;


    @Override
    public boolean deleteAllOrders() {
        inventoryItemDetailRepo.deleteAll();
        inventoryItemRepo.deleteAll();
        shipmentItemStatusRepo.deleteAll();
        orderItemBillingRepo.deleteAll();
        orderItemRepo.deleteAll();
        invoiceItemRepo.deleteAll();
        invoiceStatusRepo.deleteAll();
        paymentApplicationRepo.deleteAll();
        invoiceRepo.deleteAll();
        paymentRepo.deleteAll();
        invoiceSequenceIdRepo.deleteAll();
        paymentSequenceIdRepo.deleteAll();
        shipmentItemDeliveryPlanRepo.deleteAll();
        deliveryTripDetailStatusRepo.deleteAll();
        deliveryTripDetailRepo.deleteAll();
        shipmentItemRepo.deleteAll();
        orderHeaderRepo.deleteAll();
        shipmentRepo.deleteAll();
        deliveryTripStatusRepo.deleteAll();
        deliveryTripRepo.deleteAll();
        deliveryPlanRepo.deleteAll();
        orderHeaderSequenceIdRepo.deleteAll();
        receiptItemRepo.deleteAll();
        receiptRepo.deleteAll();
        receiptSequenceIdRepo.deleteAll();

        return true;
    }
}

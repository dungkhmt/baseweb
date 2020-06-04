package com.hust.baseweb.applications.logistics.service;

import com.hust.baseweb.applications.accounting.repo.*;
import com.hust.baseweb.applications.accounting.repo.sequenceid.InvoiceSequenceIdRepo;
import com.hust.baseweb.applications.accounting.repo.sequenceid.PaymentSequenceIdRepo;
import com.hust.baseweb.applications.logistics.repo.*;
import com.hust.baseweb.applications.order.repo.*;
import com.hust.baseweb.applications.order.repo.mongodb.CustomerRevenueRepo;
import com.hust.baseweb.applications.order.repo.mongodb.ProductRevenueRepo;
import com.hust.baseweb.applications.order.repo.mongodb.TotalRevenueRepo;
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
    private ProductFacilityRepo productFacilityRepo;
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
    private ShipmentItemRoleRepo shipmentItemRoleRepo;
    private ShipmentItemRepo shipmentItemRepo;
    private OrderRoleRepo orderRoleRepo;
    private OrderHeaderRepo orderHeaderRepo;
    private ShipmentRepo shipmentRepo;
    private DeliveryTripStatusRepo deliveryTripStatusRepo;
    private DeliveryTripRepo deliveryTripRepo;
    private DeliveryPlanRepo deliveryPlanRepo;
    private OrderHeaderSequenceIdRepo orderHeaderSequenceIdRepo;
    private ReceiptItemRepo receiptItemRepo;
    private ReceiptRepo receiptRepo;
    private ReceiptSequenceIdRepo receiptSequenceIdRepo;
    private OrderStatusRepo orderStatusRepo;
    private ProductRevenueRepo productRevenueRepo;
    private CustomerRevenueRepo customerRevenueRepo;
    private TotalRevenueRepo totalRevenueRepo;


    @Override
    public boolean deleteAllOrders() {
        inventoryItemDetailRepo.deleteAllInBatch();
        inventoryItemDetailRepo.flush();

        inventoryItemRepo.deleteAllInBatch();
        inventoryItemRepo.flush();

        productFacilityRepo.deleteAllInBatch();
        productFacilityRepo.flush();

        shipmentItemStatusRepo.deleteAllInBatch();
        shipmentItemStatusRepo.flush();

        orderItemBillingRepo.deleteAll();

        orderItemRepo.deleteAllInBatch();
        orderItemRepo.flush();

        invoiceItemRepo.deleteAll();

        invoiceStatusRepo.deleteAll();

        paymentApplicationRepo.deleteAll();

        invoiceRepo.deleteAll();

        paymentRepo.deleteAll();

        customerRevenueRepo.deleteAll();

        productRevenueRepo.deleteAll();

        totalRevenueRepo.deleteAll();

        invoiceSequenceIdRepo.deleteAllInBatch();
        invoiceSequenceIdRepo.flush();

        paymentSequenceIdRepo.deleteAllInBatch();
        paymentSequenceIdRepo.flush();

        shipmentItemDeliveryPlanRepo.deleteAllInBatch();
        shipmentItemDeliveryPlanRepo.flush();

        deliveryTripDetailStatusRepo.deleteAllInBatch();
        deliveryTripDetailStatusRepo.flush();

        deliveryTripDetailRepo.deleteAllInBatch();
        deliveryTripDetailRepo.flush();

        shipmentItemRoleRepo.deleteAllInBatch();
        shipmentItemRoleRepo.flush();

        shipmentItemRepo.deleteAllInBatch();
        shipmentItemRepo.flush();

        orderStatusRepo.deleteAllInBatch();
        orderStatusRepo.flush();

        orderRoleRepo.deleteAllInBatch();
        orderRoleRepo.flush();

        orderHeaderRepo.deleteAllInBatch();
        orderHeaderRepo.flush();

        shipmentRepo.deleteAllInBatch();
        shipmentRepo.flush();

        deliveryTripStatusRepo.deleteAllInBatch();
        deliveryTripStatusRepo.flush();

        deliveryTripRepo.deleteAllInBatch();
        deliveryTripRepo.flush();

        deliveryPlanRepo.deleteAllInBatch();
        deliveryPlanRepo.flush();

        orderHeaderSequenceIdRepo.deleteAllInBatch();
        orderHeaderSequenceIdRepo.flush();

        receiptItemRepo.deleteAllInBatch();
        receiptItemRepo.flush();

        receiptRepo.deleteAllInBatch();
        receiptRepo.flush();

        receiptSequenceIdRepo.deleteAllInBatch();
        receiptSequenceIdRepo.flush();

        return true;
    }
}

package com.hust.baseweb.applications.logistics.service;

import com.hust.baseweb.applications.accounting.document.*;
import com.hust.baseweb.applications.accounting.repo.InvoiceItemRepo;
import com.hust.baseweb.applications.accounting.repo.OrderItemBillingRepo;
import com.hust.baseweb.applications.accounting.service.InvoiceService;
import com.hust.baseweb.applications.logistics.entity.*;
import com.hust.baseweb.applications.logistics.model.ExportInventoryItemInputModel;
import com.hust.baseweb.applications.logistics.model.ExportInventoryItemsInputModel;
import com.hust.baseweb.applications.logistics.model.ImportInventoryItemInputModel;
import com.hust.baseweb.applications.logistics.model.InventoryModel;
import com.hust.baseweb.applications.logistics.repo.*;
import com.hust.baseweb.applications.order.entity.OrderHeader;
import com.hust.baseweb.applications.order.entity.OrderItem;
import com.hust.baseweb.applications.order.repo.*;
import com.hust.baseweb.applications.tms.entity.Shipment;
import com.hust.baseweb.applications.tms.entity.ShipmentItem;
import com.hust.baseweb.applications.tms.entity.status.ShipmentItemStatus;
import com.hust.baseweb.applications.tms.repo.ShipmentItemRepo;
import com.hust.baseweb.applications.tms.repo.ShipmentRepo;
import com.hust.baseweb.applications.tms.repo.status.ShipmentItemStatusRepo;
import com.hust.baseweb.entity.StatusItem;
import com.hust.baseweb.repo.StatusItemRepo;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
@Log4j2
public class InventoryItemServiceImpl implements InventoryItemService {
    public static final String module = InventoryItemServiceImpl.class.getName();

    private InventoryItemRepo inventoryItemRepo;
    private FacilityService facilityService;
    private ProductService productService;
    private ProductFacilityRepo productFacilityRepo;
    private InventoryItemDetailService inventoryItemDetailService;

    private OrderHeaderPageRepo orderHeaderPageRepo;
    private OrderHeaderRepo orderHeaderRepo;
    private OrderItemRepo orderItemRepo;
    private OrderRoleRepo orderRoleRepo;
    private PartyCustomerRepo partyCustomerRepo;

    private FacilityRepo facilityRepo;
    private ProductRepo productRepo;

    private ShipmentItemRepo shipmentItemRepo;

    private InventoryItemDetailRepo inventoryItemDetailRepo;

    private ShipmentRepo shipmentRepo;
    private StatusItemRepo statusItemRepo;
    private ShipmentItemStatusRepo shipmentItemStatusRepo;

    private ProductPriceRepo productPriceRepo;

    private InvoiceService invoiceService;
    private InvoiceItemRepo invoiceItemRepo;
    private OrderItemBillingRepo orderItemBillingRepo;


    @Override
    @Transactional
    public InventoryItem importInventoryItem(ImportInventoryItemInputModel input) {

//        System.out.println(module + "::save(" + input.getProductId() + "," + input.getQuantityOnHandTotal() + ")");

        Product product = productService.findByProductId(input.getProductId());
        Facility facility = facilityService.findFacilityById(input.getFacilityId());

        if (product == null || facility == null) {
            return null;
        }

        // product processing region
        ProductFacility productFacility = productFacilityRepo.findByProductIdAndFacilityId(product.getProductId(),
                facility.getFacilityId());
        if (productFacility == null) {
            productFacility = new ProductFacility();
            productFacility.setProductId(product.getProductId());
            productFacility.setFacilityId(facility.getFacilityId());
            productFacility.setAtpInventoryCount(input.getQuantityOnHandTotal());
            productFacility.setLastInventoryCount(0);
        }
        productFacility.setLastInventoryCount(productFacility.getLastInventoryCount() + input.getQuantityOnHandTotal());
        productFacility = productFacilityRepo.save(productFacility);

        // inventory processing region
        InventoryItem inventoryItem = new InventoryItem();
        inventoryItem.setFacility(facility);
        inventoryItem.setProduct(product);
        inventoryItem.setLotId(input.getLotId());
        inventoryItem.setUomId(input.getUomId());
        inventoryItem.setQuantityOnHandTotal(input.getQuantityOnHandTotal());

        return inventoryItemRepo.save(inventoryItem);
    }

    @Override
    @Transactional
    @org.springframework.transaction.annotation.Transactional
    public String exportInventoryItems(ExportInventoryItemsInputModel inventoryItemsInput) {
        Date now = new Date();

//        List<InventoryItem> inventoryItems = inventoryItemRepo.findAll();// to be improved, find by (productId, facilityId)

        List<Product> queryProducts = buildProducts(inventoryItemsInput);

        List<Facility> queryFacilities = buildFacilities(inventoryItemsInput);

        Map<List<String>, List<InventoryItem>> inventoryItemsMap = getInventoryItemsMap(queryProducts, queryFacilities);

        Map<List<String>, ProductFacility> productFacilityMap = getProductFacilityMap(queryProducts, queryFacilities);

        Map<List<String>, OrderItem> orderItemMap = buildOrderItemMap(inventoryItemsInput);

        Map<String, OrderHeader> orderHeaderMap = buildOrderHeaderMap(inventoryItemsInput);

        log.info("exportInventoryItems, inventoryItems.sz = " + inventoryItemsMap.size());

        List<InventoryItemDetail> inventoryItemDetails = new ArrayList<>();

        Shipment shipment = createAndSaveShipment();

        List<ShipmentItem> shipmentItems = new ArrayList<>();
        StatusItem statusItem = statusItemRepo.findById("SHIPMENT_ITEM_CREATED")
                .orElseThrow(NoSuchElementException::new);

        Map<String, ProductPrice> productPriceMap = buildProductPriceMap(queryProducts);

        Invoice invoice = createInvoice(now);
        List<InvoiceItem> invoiceItems = new ArrayList<>();
        List<OrderItemBilling> orderItemBillings = new ArrayList<>();

        for (int i = 0; i < inventoryItemsInput.getInventoryItems().length; i++) {
            ExportInventoryItemInputModel exportModel = inventoryItemsInput.getInventoryItems()[i];
            String productId = exportModel.getProductId();
            String facilityId = exportModel.getFacilityId();
            int quantity = exportModel.getQuantity();
            OrderItem orderItem = orderItemMap.get(
                    Arrays.asList(exportModel.getOrderId(), exportModel.getOrderItemSeqId()));
            OrderHeader orderHeader = orderHeaderMap.get(orderItem.getOrderId());

            log.info("exportInventoryItems, productId = " + productId + ", facilityId = " + facilityId + ", list = " +
                    inventoryItemsMap.size());

            List<InventoryItem> selectedInventoryItems = inventoryItemsMap.get(Arrays.asList(facilityId, productId));
            int totalCount = // total inventory count of productId in the facilityId
                    productFacilityMap.get(Arrays.asList(facilityId, productId)).getLastInventoryCount()
                            - quantity; // remain total inventory count
            selectedInventoryItems.sort(Comparator.comparingInt(InventoryItem::getQuantityOnHandTotal));

            for (InventoryItem inventoryItem : selectedInventoryItems) {
                if (quantity <= inventoryItem.getQuantityOnHandTotal()) {
                    InventoryItemDetail inventoryItemDetail = inventoryItemDetailService.createInventoryItemDetail(
                            inventoryItem,
                            -quantity,
                            orderItem);
                    orderItem.setExportedQuantity(orderItem.getExportedQuantity() + quantity);
                    inventoryItemDetails.add(inventoryItemDetail);
                    inventoryItem.setQuantityOnHandTotal(inventoryItem.getQuantityOnHandTotal() - quantity);

                    ShipmentItem shipmentItem = createShipmentItem(shipment,
                            statusItem,
                            quantity,
                            orderItem,
                            orderHeader,
                            inventoryItem);
                    shipmentItems.add(shipmentItem);

                    break;
                } else {
                    InventoryItemDetail inventoryItemDetail = inventoryItemDetailService.createInventoryItemDetail(
                            inventoryItem,
                            -inventoryItem.getQuantityOnHandTotal(),
                            orderItem);
                    ShipmentItem shipmentItem = createShipmentItem(shipment,
                            statusItem,
                            inventoryItem.getQuantityOnHandTotal(),
                            orderItem,
                            orderHeader,
                            inventoryItem);
                    shipmentItems.add(shipmentItem);

                    orderItem.setExportedQuantity(orderItem.getExportedQuantity() +
                            inventoryItem.getQuantityOnHandTotal());
                    inventoryItemDetails.add(inventoryItemDetail);
                    inventoryItem.setQuantityOnHandTotal(0);
                    quantity -= inventoryItem.getQuantityOnHandTotal();
                }
            }

            productFacilityMap.get(Arrays.asList(facilityId, productId)).setLastInventoryCount(Math.max(totalCount, 0));

            InvoiceItem invoiceItem = new InvoiceItem(new InvoiceItem.Id(invoice.getInvoiceId(), i + ""),
                    InvoiceItemType.SALES_INVOICE_PRODUCT_ITEM,
                    productPriceMap.getOrDefault(productId, new ProductPrice()).getPrice() * quantity,
                    "CUR_vnd",
                    now,
                    now);
            invoiceItems.add(invoiceItem);

            OrderItemBilling orderItemBilling = new OrderItemBilling(new OrderItemBilling.Id(orderItem.getOrderId(),
                    orderItem.getOrderItemSeqId(),
                    invoice.getInvoiceId(),
                    invoiceItem.getId().getInvoiceItemSeqId()),
                    quantity,
                    invoiceItem.getAmount(), "CUR_vnd", now, now);
            orderItemBillings.add(orderItemBilling);
        }

        shipmentItems = shipmentItemRepo.saveAll(shipmentItems);

        shipmentItemStatusRepo.saveAll(
                shipmentItems.stream()
                        .map(shipmentItem -> new ShipmentItemStatus(null, shipmentItem, statusItem, now, null))
                        .collect(Collectors.toList()));

        orderItemRepo.saveAll(orderItemMap.values());   // update exported quantity

        updateOrderHeaderAllExportedStatus(inventoryItemsInput);    // update all exported

        inventoryItemDetailRepo.saveAll(inventoryItemDetails);   // save export history
        productFacilityRepo.saveAll(productFacilityMap.values());   // update inventory

        invoiceItemRepo.saveAll(invoiceItems);
        orderItemBillingRepo.saveAll(orderItemBillings);

        return "ok";
    }

    private Invoice createInvoice(Date createDate) {
        return invoiceService.save(new Invoice(null,
                InvoiceType.SALES_INVOICE,
                "INVOICE_CREATED",
                createDate,
                null,
                null,
                0.0,
                "CUR_vnd",
                createDate,
                createDate));
    }

    @NotNull
    private Map<String, ProductPrice> buildProductPriceMap(List<Product> products) {
        return productPriceRepo.findAllByProductInAndThruDateNull(products)
                .stream()
                .collect(Collectors.toMap(productPrice -> productPrice.getProduct().getProductId(), p -> p));
    }

    @NotNull
    private ShipmentItem createShipmentItem(Shipment shipment,
                                            StatusItem statusItem,
                                            int quantity,
                                            OrderItem orderItem,
                                            OrderHeader orderHeader, InventoryItem inventoryItem) {
        ShipmentItem shipmentItem = new ShipmentItem();
        shipmentItem.setShipment(shipment);
        shipmentItem.setFacility(inventoryItem.getFacility());
        shipmentItem.setStatusItem(statusItem);
        shipmentItem.setQuantity(quantity);
        shipmentItem.setOrderItem(orderItem);

        //shipmentItem.setCustomer(orderHeader.getPartyCustomer());
        shipmentItem.setPartyCustomer(orderHeader.getPartyCustomer());

        shipmentItem.setShipToLocation(orderHeader.getShipToPostalAddress());
        return shipmentItem;
    }

    @NotNull
    private Map<String, OrderHeader> buildOrderHeaderMap(ExportInventoryItemsInputModel inventoryItemsInput) {
        return orderHeaderRepo.findAllByOrderIdIn(Stream.of(inventoryItemsInput.getInventoryItems())
                .map(ExportInventoryItemInputModel::getOrderId).distinct()
                .collect(Collectors.toList()))
                .stream()
                .collect(Collectors.toMap(OrderHeader::getOrderId, orderHeader -> orderHeader));
    }

    @NotNull
    private Shipment createAndSaveShipment() {
        Shipment shipment = new Shipment();
        shipment.setShipmentTypeId("SALES_SHIPMENT");
        shipment = shipmentRepo.save(shipment);
        return shipment;
    }

    private void updateOrderHeaderAllExportedStatus(ExportInventoryItemsInputModel inventoryItemsInput) {
        if (inventoryItemsInput.getInventoryItems().length > 0) {
            OrderHeader orderHeader = orderHeaderRepo.findById(inventoryItemsInput.getInventoryItems()[0].getOrderId())
                    .orElseThrow(NoSuchElementException::new);
            List<OrderItem> orderItems = orderItemRepo.findAllByOrderId(orderHeader.getOrderId());
            boolean exported = true;
            for (OrderItem orderItem : orderItems) {
                if (!orderItem.getExportedQuantity().equals(orderItem.getQuantity())) {
                    exported = false;
                    break;
                }
            }
            orderHeader.setExported(exported);
            orderHeaderRepo.save(orderHeader);
        }
    }

    @NotNull
    private List<Product> buildProducts(ExportInventoryItemsInputModel inventoryItemsInput) {
        return Stream.of(inventoryItemsInput.getInventoryItems())
                .map(exportInventoryItemInputModel -> {
                    Product product = new Product();
                    product.setProductId(exportInventoryItemInputModel.getProductId());
                    return product;
                })
                .collect(Collectors.toList());
    }

    @NotNull
    private List<Facility> buildFacilities(ExportInventoryItemsInputModel inventoryItemsInput) {
        return Stream.of(inventoryItemsInput.getInventoryItems())
                .map(exportInventoryItemInputModel -> {
                    Facility facility = new Facility();
                    facility.setFacilityId(exportInventoryItemInputModel.getFacilityId());
                    return facility;
                })
                .collect(Collectors.toList());
    }

    @NotNull
    private Map<List<String>, OrderItem> buildOrderItemMap(ExportInventoryItemsInputModel inventoryItemsInput) {
        return orderItemRepo.findAllByOrderIdInAndOrderItemSeqIdIn(
                Stream.of(inventoryItemsInput.getInventoryItems())
                        .map(ExportInventoryItemInputModel::getOrderId).distinct()
                        .collect(Collectors.toList()),
                Stream.of(inventoryItemsInput.getInventoryItems())
                        .map(ExportInventoryItemInputModel::getOrderItemSeqId).distinct()
                        .collect(Collectors.toList())
        ).stream().collect(Collectors.toMap(orderItem -> Arrays.asList(orderItem.getOrderId(),
                orderItem.getOrderItemSeqId()),
                orderItem -> orderItem));
    }

    @NotNull
    private Map<List<String>, ProductFacility> getProductFacilityMap(List<Product> queryProducts,
                                                                     List<Facility> queryFacilities) {
        return productFacilityRepo.findByProductIdInAndFacilityIdIn(
                queryProducts.stream().map(Product::getProductId).collect(Collectors.toList()),
                queryFacilities.stream().map(Facility::getFacilityId).collect(Collectors.toList())
        ).stream().collect(Collectors.toMap(productFacility -> Arrays.asList(productFacility.getFacilityId(),
                productFacility.getProductId()),
                productFacility -> productFacility));
    }

    @NotNull
    private Map<List<String>, List<InventoryItem>> getInventoryItemsMap(List<Product> queryProducts,
                                                                        List<Facility> queryFacilities) {
        Map<List<String>, List<InventoryItem>> inventoryItemsMap = new HashMap<>();
        inventoryItemRepo.findAllByProductInAndFacilityInAndQuantityOnHandTotalGreaterThan(
                queryProducts, queryFacilities, 0).forEach(inventoryItem ->
                inventoryItemsMap.computeIfAbsent(
                        Arrays.asList(inventoryItem.getFacility().getFacilityId(),
                                inventoryItem.getProduct().getProductId()),
                        key -> new ArrayList<>())
                        .add(inventoryItem));
        return inventoryItemsMap;
    }

    @Override
    public Page<InventoryModel.OrderHeader> getInventoryOrderHeaderPage(Pageable page) {
        return orderHeaderPageRepo.findAllByExportedFalse(page).map(OrderHeader::toOrderHeaderModel);
    }

    @Override
    public List<InventoryModel.OrderItem> getInventoryOrderHeaderDetail(String facilityId, String orderId) {
        List<OrderItem> orderItems = orderItemRepo.findAllByOrderId(orderId);
        return convertOrderItemToModel(facilityId, orderItems);
    }

    @NotNull
    private List<InventoryModel.OrderItem> convertOrderItemToModel(String facilityId,
                                                                   List<OrderItem> orderItems) {
        List<InventoryItemDetail> inventoryItemDetails = inventoryItemDetailRepo.findAllByOrderItemIn(orderItems);
        Map<String, Integer> exportedQuantityCounter = new HashMap<>();
        for (InventoryItemDetail inventoryItemDetail : inventoryItemDetails) {
            exportedQuantityCounter.merge(inventoryItemDetail.getOrderItem().getOrderItemSeqId(),
                    inventoryItemDetail.getQuantityOnHandDiff(),
                    Integer::sum);
        }
        List<String> productIds = orderItems.stream()
                .map(orderItem -> orderItem.getProduct().getProductId())
                .distinct()
                .collect(Collectors.toList());
        Map<String, Integer> productFacilityCountMap;
        if (facilityId == null) {
            productFacilityCountMap = productFacilityRepo.findAllByProductIdIn(productIds)
                    .stream()
                    .collect(Collectors.toMap(ProductFacility::getProductId,
                            ProductFacility::getLastInventoryCount,
                            Integer::sum));
        } else {
            productFacilityCountMap = productFacilityRepo.findAllByFacilityIdAndProductIdIn(facilityId, productIds)
                    .stream()
                    .collect(Collectors.toMap(ProductFacility::getProductId,
                            ProductFacility::getLastInventoryCount,
                            Integer::sum));
        }
        Map<OrderItem, Integer> orderItemInventoryQuantityMap = new HashMap<>();
        for (OrderItem orderItem : orderItems) {
            orderItemInventoryQuantityMap.put(orderItem,
                    productFacilityCountMap.getOrDefault(orderItem.getProduct().getProductId(), 0));
        }

        return orderItems.stream()
                .map(orderItem -> orderItem.toOrderItemModel(
                        exportedQuantityCounter.getOrDefault(orderItem.getOrderItemSeqId(), 0),
                        orderItemInventoryQuantityMap.get(orderItem)))
                .collect(Collectors.toList());
    }

    @Override
    public List<InventoryModel.ExportDetail> getInventoryExportList(String facilityId) {
        List<ShipmentItem> shipmentItems = shipmentItemRepo.findAllByFacility(new Facility(facilityId));
        List<InventoryItemDetail> inventoryItemDetails = inventoryItemDetailRepo.findAllByOrderItemIn(shipmentItems.stream()
                .map(ShipmentItem::getOrderItem)
                .collect(Collectors.toList()));
        return inventoryItemDetails.stream()
                .map(InventoryItemDetail::toInventoryExportDetail)
                .collect(Collectors.toList());
    }

    @Override
    public List<InventoryModel.ProductFacility> getInventoryList(String facilityId) {
        Facility facility = facilityRepo.findById(facilityId).orElseThrow(NoSuchElementException::new);
        List<ProductFacility> productFacilities = productFacilityRepo.findAllByFacilityId(facilityId);
        List<String> productIds = productFacilities.stream()
                .map(ProductFacility::getProductId)
                .distinct()
                .collect(Collectors.toList());

        Map<String, Product> productMap = productRepo.findAllByProductIdIn(productIds).stream()
                .collect(Collectors.toMap(Product::getProductId, product -> product));

        return productFacilities.stream().map(productFacility -> new InventoryModel.ProductFacility(
                productFacility.getProductId(),
                productMap.get(productFacility.getProductId()).getProductName(),
                productFacility.getLastInventoryCount(),
                facilityId,
                facility.getFacilityName()
        )).collect(Collectors.toList());
    }
}

package com.hust.baseweb.applications.logistics.service;

import com.hust.baseweb.applications.customer.entity.PartyCustomer;
import com.hust.baseweb.applications.logistics.entity.*;
import com.hust.baseweb.applications.logistics.model.ExportInventoryItemInputModel;
import com.hust.baseweb.applications.logistics.model.ExportInventoryItemsInputModel;
import com.hust.baseweb.applications.logistics.model.ImportInventoryItemInputModel;
import com.hust.baseweb.applications.logistics.model.InventoryModel;
import com.hust.baseweb.applications.logistics.repo.InventoryItemDetailRepo;
import com.hust.baseweb.applications.logistics.repo.InventoryItemRepo;
import com.hust.baseweb.applications.logistics.repo.ProductFacilityRepo;
import com.hust.baseweb.applications.order.entity.OrderHeader;
import com.hust.baseweb.applications.order.entity.OrderItem;
import com.hust.baseweb.applications.order.entity.OrderRole;
import com.hust.baseweb.applications.order.repo.*;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
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

    private InventoryItemDetailRepo inventoryItemDetailRepo;

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
            productFacility.setAtpInventoryCount(new BigDecimal(input.getQuantityOnHandTotal()));
            productFacility.setLastInventoryCount(new BigDecimal(0));
        }
        productFacility.setLastInventoryCount(productFacility.getLastInventoryCount()
                .add(new BigDecimal(input.getQuantityOnHandTotal())));
        productFacility = productFacilityRepo.save(productFacility);

        // inventory processing region
        InventoryItem inventoryItem = new InventoryItem();
        inventoryItem.setFacility(facility);
        inventoryItem.setProduct(product);
        inventoryItem.setLotId(input.getLotId());
        inventoryItem.setQuantityOnHandTotal(input.getQuantityOnHandTotal());

        return inventoryItemRepo.save(inventoryItem);
    }

    @Override
    @Transactional
    public String exportInventoryItems(ExportInventoryItemsInputModel inventoryItemsInput) {

//        List<InventoryItem> inventoryItems = inventoryItemRepo.findAll();// to be improved, find by (productId, facilityId)
        List<Product> queryProducts = Stream.of(inventoryItemsInput.getInventoryItems())
                .map(exportInventoryItemInputModel -> {
                    Product product = new Product();
                    product.setProductId(exportInventoryItemInputModel.getProductId());
                    return product;
                })
                .collect(Collectors.toList());
        List<Facility> queryFacilities = Stream.of(inventoryItemsInput.getInventoryItems())
                .map(exportInventoryItemInputModel -> {
                    Facility facility = new Facility();
                    facility.setFacilityId(exportInventoryItemInputModel.getFacilityId());
                    return facility;
                })
                .collect(Collectors.toList());
        List<InventoryItem> inventoryItems = inventoryItemRepo.findAllByProductInAndFacilityIn(
                queryProducts, queryFacilities);

        log.info("exportInventoryItems, inventoryItems.sz = " + inventoryItems.size());

        for (int i = 0; i < inventoryItemsInput.getInventoryItems().length; i++) {
            ExportInventoryItemInputModel exportModel = inventoryItemsInput.getInventoryItems()[i];
            String productId = exportModel.getProductId();
            String facilityId = exportModel.getFacilityId();
            int quantity = exportModel.getQuantity();
            // find list of inventory-items suitable for exporting productId at the facilityId
            //List<InventoryItem> inventoryItems = inventoryItemRepo.findAllByProductIdAndFacilityId(productId, facilityId);

            log.info("exportInventoryItems, productId = " +
                    productId +
                    ", facilityId = " +
                    facilityId +
                    ", list = " +
                    inventoryItems.size());
            List<InventoryItem> selectedInventoryItems = new ArrayList<>();
            BigDecimal totalCount = new BigDecimal(0);// total inventory count of productId in the faicilityId
            for (InventoryItem inventoryItem : inventoryItems) {
                if (inventoryItem.getQuantityOnHandTotal() > 0 &&
                        inventoryItem.getProduct().getProductId().equals(productId) &&
                        inventoryItem.getFacility().getFacilityId().equals(facilityId)) {
                    log.info("exportInventoryItems, productId = " +
                            productId +
                            ", facilityId = " +
                            facilityId +
                            ", qty = " +
                            inventoryItem.getQuantityOnHandTotal());
                    selectedInventoryItems.add(inventoryItem);
                    totalCount = totalCount.add(new BigDecimal(inventoryItem.getQuantityOnHandTotal()));
                }
            }
            InventoryItem[] sortedInventoryItems = new InventoryItem[selectedInventoryItems.size()];
            for (int j = 0; j < selectedInventoryItems.size(); j++) {
                sortedInventoryItems[j] = selectedInventoryItems.get(j);
            }
            // sorting
            for (int j1 = 0; j1 < sortedInventoryItems.length; j1++) {
                for (int j2 = j1 + 1; j2 < sortedInventoryItems.length; j2++) {
                    if (sortedInventoryItems[j1].getQuantityOnHandTotal() >
                            sortedInventoryItems[j2].getQuantityOnHandTotal()) {
                        InventoryItem temp = sortedInventoryItems[j1];
                        sortedInventoryItems[j1] = sortedInventoryItems[j2];
                        sortedInventoryItems[j2] = temp;
                    }
                }
            }

            for (InventoryItem inventoryItem : sortedInventoryItems) {
                if (quantity <= inventoryItem.getQuantityOnHandTotal()) {
                    InventoryItemDetail inventoryItemDetail = new InventoryItemDetail();
                    Date effectiveDate = new Date();
                    //iid.setEffectiveDate(effectiveDate);
                    //iid.setInventoryItem(sort_list[j]);
                    //iid.setQuantityOnHandDiff(-qty);
                    inventoryItemDetail = inventoryItemDetailService.save(inventoryItem.getInventoryItemId(),
                            -quantity,
                            effectiveDate);

                    inventoryItem.setQuantityOnHandTotal(inventoryItem.getQuantityOnHandTotal() - quantity);
                    inventoryItemRepo.save(inventoryItem);
                    break;
                } else {
                    InventoryItemDetail inventoryItemDetail = new InventoryItemDetail();
                    Date effectiveDate = new Date();
                    //iid.setEffectiveDate(effectiveDate);
                    //iid.setInventoryItem(sort_list[j]);
                    //iid.setQuantityOnHandDiff(-sort_list[j].getQuantityOnHandTotal());
                    inventoryItemDetail = inventoryItemDetailService.save(inventoryItem.getInventoryItemId(),
                            -inventoryItem.getQuantityOnHandTotal(),
                            effectiveDate);

                    inventoryItem.setQuantityOnHandTotal(0);
                    inventoryItemRepo.save(inventoryItem);

                }
            }
            totalCount = totalCount.subtract(new BigDecimal(quantity));// remain total inventory count

            ProductFacility productFacility = productFacilityRepo.findByProductIdAndFacilityId(productId, facilityId);
            if (productFacility == null) {
                productFacility = new ProductFacility();
                productFacility.setProductId(productId);
                productFacility.setFacilityId(facilityId);
                productFacility.setAtpInventoryCount(totalCount);
            }
            productFacility.setLastInventoryCount(totalCount);
            productFacility = productFacilityRepo.save(productFacility);

        }
        return "ok";
    }

    @Override
    public Page<InventoryModel.OrderHeader> getInventoryOrderHeaderPage(Pageable page) {
        Page<OrderHeader> orderHeaderPage = orderHeaderPageRepo.findAll(page);
        List<String> orderIds = orderHeaderPage.stream()
                .map(OrderHeader::getOrderId)
                .distinct()
                .collect(Collectors.toList());
        List<OrderRole> orderRoles = orderRoleRepo.findAllByOrderIdIn(orderIds);
        Map<String, UUID> orderIdToPartyIdMap = new HashMap<>();
        // TODO: map 1-1
        orderRoles.forEach(orderRole -> orderIdToPartyIdMap.put(orderRole.getOrderId(), orderRole.getPartyId()));
        List<UUID> partyIds = orderRoles.stream().map(OrderRole::getPartyId).distinct().collect(Collectors.toList());
        List<PartyCustomer> partyCustomers = partyCustomerRepo.findAllByPartyIdIn(partyIds);
        Map<UUID, PartyCustomer> partyCustomerMap = new HashMap<>();
        partyCustomers.forEach(partyCustomer -> partyCustomerMap.put(partyCustomer.getPartyId(), partyCustomer));
        return orderHeaderPage.map(orderHeader -> orderHeader.toOrderHeaderModel(partyCustomerMap.getOrDefault(
                orderIdToPartyIdMap.getOrDefault(orderHeader.getOrderId(), null), null)));
    }

    @Override
    public List<InventoryModel.OrderItem> getInventoryOrderHeaderDetail(String orderId) {
        List<OrderItem> orderItems = orderItemRepo.findAllByOrderId(orderId);
        return convertOrderItemToModel(orderId, orderItems);
    }

    @NotNull
    private List<InventoryModel.OrderItem> convertOrderItemToModel(String orderId, List<OrderItem> orderItems) {
        List<InventoryItemDetail> inventoryItemDetails = inventoryItemDetailRepo.findAllByOrderIdInAndOrderItemSeqIdIn(
                Collections.singletonList(orderId),
                orderItems.stream().map(OrderItem::getOrderItemSeqId).collect(Collectors.toList())
        );
        Map<String, Integer> exportedQuantityCounter = new HashMap<>();
        for (InventoryItemDetail inventoryItemDetail : inventoryItemDetails) {
            exportedQuantityCounter.merge(inventoryItemDetail.getOrderItemSeqId(),
                    inventoryItemDetail.getQuantityOnHandDiff(),
                    Integer::sum);
        }

        return orderItems.stream()
                .map(orderItem -> orderItem.toOrderItemModel(
                        exportedQuantityCounter.getOrDefault(orderItem.getOrderItemSeqId(), 0)))
                .collect(Collectors.toList());
    }

    @Override
    public List<InventoryModel.OrderItem> getInventoryOrderDetailPage(String orderId, String facilityId) {
        List<OrderItem> orderItems
                = orderItemRepo.findAllByOrderIdAndFacility(orderId, new Facility(facilityId));
        return convertOrderItemToModel(orderId, orderItems);
    }
}

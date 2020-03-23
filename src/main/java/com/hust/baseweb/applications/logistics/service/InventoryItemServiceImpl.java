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
            productFacility.setAtpInventoryCount((double) input.getQuantityOnHandTotal());
            productFacility.setLastInventoryCount(0.0);
        }
        productFacility.setLastInventoryCount(productFacility.getLastInventoryCount() + input.getQuantityOnHandTotal());
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

        Stream<ExportInventoryItemInputModel> exportModelStream = Stream.of(inventoryItemsInput.getInventoryItems());

        List<Product> queryProducts = getProducts(exportModelStream);

        List<Facility> queryFacilities = getFacilities(exportModelStream);

        Map<List<String>, List<InventoryItem>> inventoryItemsMap = getInventoryItemsMap(queryProducts, queryFacilities);

        Map<List<String>, ProductFacility> productFacilityMap = getProductFacilityMap(queryProducts, queryFacilities);

        Map<List<String>, OrderItem> orderItemMap = getOrderItemMap(exportModelStream);

        log.info("exportInventoryItems, inventoryItems.sz = " + inventoryItemsMap.size());

        List<InventoryItemDetail> inventoryItemDetails = new ArrayList<>();

        for (int i = 0; i < inventoryItemsInput.getInventoryItems().length; i++) {
            ExportInventoryItemInputModel exportModel = inventoryItemsInput.getInventoryItems()[i];
            String productId = exportModel.getProductId();
            String facilityId = exportModel.getFacilityId();
            int quantity = exportModel.getQuantity();
            OrderItem orderItem = orderItemMap.get(
                    Arrays.asList(exportModel.getOrderId(), exportModel.getOrderItemSeqId()));

            log.info("exportInventoryItems, productId = " + productId + ", facilityId = " + facilityId + ", list = " +
                    inventoryItemsMap.size());

            List<InventoryItem> selectedInventoryItems = inventoryItemsMap.get(Arrays.asList(facilityId, productId));
            double totalCount = // total inventory count of productId in the faicilityId
                    selectedInventoryItems.stream().mapToInt(InventoryItem::getQuantityOnHandTotal).sum()
                            - quantity; // remain total inventory count
            selectedInventoryItems.sort(Comparator.comparingInt(InventoryItem::getQuantityOnHandTotal));

            for (InventoryItem inventoryItem : selectedInventoryItems) {
                if (quantity <= inventoryItem.getQuantityOnHandTotal()) {
                    InventoryItemDetail inventoryItemDetail = inventoryItemDetailService.createInventoryItemDetail(
                            inventoryItem,
                            -quantity,
                            orderItem);
                    inventoryItemDetails.add(inventoryItemDetail);
                    inventoryItem.setQuantityOnHandTotal(inventoryItem.getQuantityOnHandTotal() - quantity);
                    break;
                } else {
                    InventoryItemDetail inventoryItemDetail = inventoryItemDetailService.createInventoryItemDetail(
                            inventoryItem,
                            -inventoryItem.getQuantityOnHandTotal(),
                            orderItem);
                    inventoryItemDetails.add(inventoryItemDetail);
                    inventoryItem.setQuantityOnHandTotal(0);
                    quantity -= inventoryItem.getQuantityOnHandTotal();
                }
            }

            productFacilityMap.computeIfAbsent(Arrays.asList(facilityId, productId), key -> {
                ProductFacility productFacility = new ProductFacility();
                productFacility.setProductId(productId);
                productFacility.setFacilityId(facilityId);
                productFacility.setAtpInventoryCount(totalCount);
                return productFacility;
            }).setLastInventoryCount(totalCount);
        }

        inventoryItemDetailRepo.saveAll(inventoryItemDetails);
        inventoryItemRepo.saveAll(inventoryItemsMap.values()
                .stream()
                .flatMap(Collection::stream)
                .collect(Collectors.toList()));    // convert List<List<T>> --> List<T>
        productFacilityRepo.saveAll(productFacilityMap.values());

        return "ok";
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

    @NotNull
    private List<Facility> getFacilities(Stream<ExportInventoryItemInputModel> exportModelStream) {
        return exportModelStream
                .map(exportInventoryItemInputModel -> {
                    Facility facility = new Facility();
                    facility.setFacilityId(exportInventoryItemInputModel.getFacilityId());
                    return facility;
                })
                .collect(Collectors.toList());
    }

    @NotNull
    private List<Product> getProducts(Stream<ExportInventoryItemInputModel> exportModelStream) {
        return exportModelStream
                .map(exportInventoryItemInputModel -> {
                    Product product = new Product();
                    product.setProductId(exportInventoryItemInputModel.getProductId());
                    return product;
                })
                .collect(Collectors.toList());
    }

    @NotNull
    private Map<List<String>, OrderItem> getOrderItemMap(Stream<ExportInventoryItemInputModel> exportModelStream) {
        return orderItemRepo.findAllByOrderIdInAndOrderItemSeqIdIn(
                exportModelStream.map(ExportInventoryItemInputModel::getOrderId).collect(Collectors.toList()),
                exportModelStream.map(ExportInventoryItemInputModel::getOrderItemSeqId).collect(Collectors.toList())
        ).stream().collect(Collectors.toMap(orderItem -> Arrays.asList(orderItem.getOrderId(),
                orderItem.getOrderItemSeqId()),
                orderItem -> orderItem));
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
        return convertOrderItemToModel(orderItems);
    }

    @NotNull
    private List<InventoryModel.OrderItem> convertOrderItemToModel(List<OrderItem> orderItems) {
        List<InventoryItemDetail> inventoryItemDetails = inventoryItemDetailRepo.findAllByOrderItemIn(orderItems);
        Map<String, Integer> exportedQuantityCounter = new HashMap<>();
        for (InventoryItemDetail inventoryItemDetail : inventoryItemDetails) {
            exportedQuantityCounter.merge(inventoryItemDetail.getOrderItem().getOrderItemSeqId(),
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
        return convertOrderItemToModel(orderItems);
    }
}

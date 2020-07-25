package com.hust.baseweb.applications.specialpurpose.saleslogmongo.service;

import com.hust.baseweb.applications.specialpurpose.saleslogmongo.common.UserLoginFacilityRelationType;
import com.hust.baseweb.applications.specialpurpose.saleslogmongo.document.*;
import com.hust.baseweb.applications.specialpurpose.saleslogmongo.model.CreatePurchaseOrderInputModel;
import com.hust.baseweb.applications.specialpurpose.saleslogmongo.model.CreateSalesOrderInputModel;
import com.hust.baseweb.applications.specialpurpose.saleslogmongo.model.GetInventoryItemOutputModel;
import com.hust.baseweb.applications.specialpurpose.saleslogmongo.repository.*;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.bson.types.ObjectId;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Hien Hoang (hienhoang2702@gmail.com)
 */
@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
@Log4j2
public class LogisticServiceImpl implements LogisticService {

    private final FacilityRepository facilityRepository;
    private final InventoryItemRepository inventoryItemRepository;
    private final InventoryItemDetailRepository inventoryItemDetailRepository;
    private final ProductRepository productRepository;

    private final OrderItemRepository orderItemRepository;
    private final PurchaseOrderRepository purchaseOrderRepository;

    private final UserLoginFacilityRepository userLoginFacilityRepository;
    private final OrganizationRepository organizationRepository;

    @Override
    public PurchaseOrder createPurchaseOrder(CreatePurchaseOrderInputModel input) {
        ModelMapper modelMapper = new ModelMapper();

        List<OrderItem> orderItems = input
            .getOrderItems()
            .stream()
            .map(orderItemModel -> modelMapper.map(orderItemModel, OrderItem.class))
            .collect(Collectors.toList());

        orderItems = orderItemRepository.saveAll(orderItems);

        PurchaseOrder purchaseOrder = input.toPurchaseOrder();
        purchaseOrder.setOrderItemIds(orderItems.stream().map(OrderItem::getOrderItemId).collect(Collectors.toList()));

        purchaseOrder = purchaseOrderRepository.save(purchaseOrder);

        /*
         * Nhập kho
         */
        Map<String, Integer> productQuantityMap = new HashMap<>();

        for (OrderItem orderItem : orderItems) {
            productQuantityMap.merge(orderItem.getProductId(), orderItem.getQuantity(), Integer::sum);
        }

        List<InventoryItem> inventoryItems = new ArrayList<>();

        for (Map.Entry<String, Integer> entry : productQuantityMap.entrySet()) {
            inventoryItems.add(new InventoryItem(
                null,
                entry.getKey(),
                purchaseOrder.getToFacilityId(),
                entry.getValue()));
        }

        inventoryItems = inventoryItemRepository.saveAll(inventoryItems);

        return purchaseOrder;
    }

    @Override
    public GetInventoryItemOutputModel getInventoryItems(String facilityId) {
        Facility facility = facilityRepository.findById(facilityId).orElse(null);
        if (facility == null) {
            return new GetInventoryItemOutputModel();
        }

        List<InventoryItem> inventoryItems = inventoryItemRepository.findAllByFacilityId(facilityId);
        Map<ObjectId, InventoryItem> inventoryItemMap = inventoryItems
            .stream()
            .collect(Collectors.toMap(InventoryItem::getInventoryItemId, i -> i));

        List<InventoryItemDetail> inventoryItemDetails = inventoryItemDetailRepository.findAllByInventoryItemIdIn(
            inventoryItemMap.keySet());

        Map<ObjectId, List<InventoryItemDetail>> inventoryItemIdToDetails = inventoryItemDetails
            .stream()
            .collect(Collectors.groupingBy(InventoryItemDetail::getInventoryItemId));

        Map<String, Integer> productQuantityMap = new HashMap<>();

        for (Map.Entry<ObjectId, List<InventoryItemDetail>> entry : inventoryItemIdToDetails.entrySet()) {
            InventoryItem inventoryItem = inventoryItemMap.get(entry.getKey());
            int totalQuantity = inventoryItem.getQuantityOnHandTotal();
            for (InventoryItemDetail inventoryItemDetail : entry.getValue()) {
                totalQuantity -= inventoryItemDetail.getQuantityOnHandDiff();
            }

            productQuantityMap.merge(inventoryItem.getProductId(), totalQuantity, Integer::sum);
        }

        GetInventoryItemOutputModel getInventoryItemOutputModel = new GetInventoryItemOutputModel();
        getInventoryItemOutputModel.setFacility(facility);
        getInventoryItemOutputModel.setProductQuantities(new ArrayList<>());

        Map<String, Product> productMap = productRepository
            .findAllByProductIdIn(productQuantityMap.keySet())
            .stream()
            .collect(Collectors.toMap(Product::getProductId, p -> p));

        for (Map.Entry<String, Integer> entry : productQuantityMap.entrySet()) {
            getInventoryItemOutputModel
                .getProductQuantities()
                .add(new GetInventoryItemOutputModel.ProductQuantity(productMap.get(entry.getKey()), entry.getValue()));
        }

        return getInventoryItemOutputModel;
    }

    @Override
    public List<Facility> getFacilityOfSalesman(String salesmanId) {
        List<UserLoginFacility> userLoginFacilities = userLoginFacilityRepository.findByUserLoginIdAndUserLoginFacilityRelationTypeAndThruDate(salesmanId,
                                                                                                        UserLoginFacilityRelationType.SALESMAN_SELL_FROM_FACILITY,
                                                                                                        null);
        List<Facility> facilities = new ArrayList<Facility>();
        log.info("getFacilityOfSalesman, salesmanId = " + salesmanId + ", ret userLoginFacilities.sz = " + userLoginFacilities.size());

        for(UserLoginFacility uf: userLoginFacilities){
            Facility facility = facilityRepository.findByFacilityId(uf.getFacilityId());
            log.info("getFacilityOfSalesman, facility in userLoginFacility " + uf.getFacilityId());
            facilities.add(facility);
        }
        return facilities;
    }

    @Override
    public Facility createFacilityOfSalesman(String salesmanId, String facilityName, String address) {
        log.info("createFacilityOfSalesman, salesmanId = " + salesmanId + " facilityName = " + facilityName + ", address = " + address);
        String facilityId = UUID.randomUUID().toString();
        Organization organization = new Organization(facilityId,facilityName,address);
        organizationRepository.save(organization);
        Facility facility = new Facility(facilityId);
        facilityRepository.save(facility);
        UserLoginFacility userLoginFacility = new UserLoginFacility();
        userLoginFacility.setUserLoginId(salesmanId);
        userLoginFacility.setFacilityId(facilityId);
        userLoginFacility.setUserLoginFacilityRelationType(UserLoginFacilityRelationType.SALESMAN_SELL_FROM_FACILITY);
        userLoginFacility.setFromDate(new Date());
        userLoginFacility.setThruDate(null);

        userLoginFacilityRepository.save(userLoginFacility);

        return facility;
    }


}

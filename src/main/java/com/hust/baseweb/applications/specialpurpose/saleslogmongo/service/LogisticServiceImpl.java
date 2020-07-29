package com.hust.baseweb.applications.specialpurpose.saleslogmongo.service;

import com.hust.baseweb.applications.order.entity.OrderHeader;
import com.hust.baseweb.applications.specialpurpose.saleslogmongo.common.UserLoginFacilityRelationType;
import com.hust.baseweb.applications.specialpurpose.saleslogmongo.document.*;
import com.hust.baseweb.applications.specialpurpose.saleslogmongo.model.CreatePurchaseOrderInputModel;
import com.hust.baseweb.applications.specialpurpose.saleslogmongo.model.FacilityModel;
import com.hust.baseweb.applications.specialpurpose.saleslogmongo.model.GetInventoryItemOutputModel;
import com.hust.baseweb.applications.specialpurpose.saleslogmongo.repository.*;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.bson.types.ObjectId;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private final ProductFacilityRepository productFacilityRepository;

    @Override
    @Transactional
    public PurchaseOrder createPurchaseOrder(CreatePurchaseOrderInputModel input) {
        ModelMapper modelMapper = new ModelMapper();

        List<OrderItem> orderItems = input
            .getOrderItems()
            .stream()
            .map(orderItemModel -> modelMapper.map(orderItemModel, OrderItem.class))
            .collect(Collectors.toList());

        //orderItems = orderItemRepository.saveAll(orderItems);

        PurchaseOrder purchaseOrder = input.toPurchaseOrder();
        //purchaseOrder.setOrderItemIds(orderItems.stream().map(OrderItem::getOrderItemId).collect(Collectors.toList()));
        purchaseOrder.setOrderItems(orderItems);

        purchaseOrder = purchaseOrderRepository.save(purchaseOrder);

        /*
         * Nhập kho
         */
        Map<String, Integer> productQuantityMap = new HashMap<>();

        for (OrderItem orderItem : orderItems) {
            productQuantityMap.merge(orderItem.getProductId(), orderItem.getQuantity(), Integer::sum);
        }

        List<InventoryItem> inventoryItems = new ArrayList<>();
        List<ProductFacility> productFacilities = new ArrayList<>();

        for (Map.Entry<String, Integer> entry : productQuantityMap.entrySet()) {
            inventoryItems.add(new InventoryItem(
                null,
                entry.getKey(),
                purchaseOrder.getToFacilityId(),
                entry.getValue()));

            // UPDATE product-facility
            // DONE to be improved
            ProductFacility productFacility = productFacilityRepository
                .findById(new ProductFacility.ProductFacilityId(entry.getKey(), input.getToFacilityId()))
                .orElseGet(() -> {
                    ProductFacility newProductFacility = new ProductFacility();
                    newProductFacility.setId(new ProductFacility.ProductFacilityId(
                        entry.getKey(),
                        input.getToFacilityId()));
                    newProductFacility.setQuantityOnHand(0);
                    return newProductFacility;
                });
            productFacility.setQuantityOnHand(productFacility.getQuantityOnHand() + entry.getValue());

//            List<ProductFacility> productFacilities = productFacilityRepository.findAllByProductIdAndFacilityId(
//                entry.getKey(),
//                input.getToFacilityId());
//            if (productFacilities != null && productFacilities.size() > 0) {
//                productFacility = productFacilities.get(0);
//                productFacility.setQuantityOnHand(productFacility.getQuantityOnHand() + entry.getValue());
//            } else {
//                productFacility = new ProductFacility();
//                productFacility.setId(new ProductFacility.ProductFacilityId(entry.getKey(), input.getToFacilityId()));
//                productFacility.setQuantityOnHand(entry.getValue());
//            }
            productFacilities.add(productFacility);
        }

        inventoryItems = inventoryItemRepository.saveAll(inventoryItems);

        productFacilityRepository.saveAll(productFacilities);

        return purchaseOrder;
    }

    @Override
    public GetInventoryItemOutputModel getInventoryItems(String facilityId) {
        List<ProductFacility> productFacilities = productFacilityRepository.findAllById_FacilityId(facilityId);
        GetInventoryItemOutputModel getInventoryItemOutputModel = new GetInventoryItemOutputModel();
        Facility facility = facilityRepository.findByFacilityId(facilityId);
        getInventoryItemOutputModel.setFacility(facility);
        List<GetInventoryItemOutputModel.ProductQuantity> productQuantities = new ArrayList<>();
        for(ProductFacility productFacility: productFacilities){
            Product product = productRepository.findByProductId(productFacility.getId().getProductId());
            int qty = productFacility.getQuantityOnHand();
            productQuantities.add(new GetInventoryItemOutputModel.ProductQuantity(product,qty));
        }
        getInventoryItemOutputModel.setProductQuantities(productQuantities);

        return getInventoryItemOutputModel;

        /*

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
        */
    }


    @Override
    public List<FacilityModel> getFacilityOfSalesman(String salesmanId) {
        List<UserLoginFacility> userLoginFacilities = userLoginFacilityRepository.findByUserLoginIdAndUserLoginFacilityRelationTypeAndThruDate(
            salesmanId,
            UserLoginFacilityRelationType.SALESMAN_SELL_FROM_FACILITY,
            null);

        List<String> facilityIds = userLoginFacilities
            .stream()
            .map(UserLoginFacility::getFacilityId)
            .distinct()
            .collect(Collectors.toList());

        List<Facility> facility = facilityRepository.findAllByFacilityIdIn(facilityIds);
        List<FacilityModel> facilityModels = new ArrayList<FacilityModel>();
        List<Organization> organizations = organizationRepository.findAllByOrganizationIdIn(facilityIds);
        for(Organization organization: organizations){
            FacilityModel facilityModel = new FacilityModel(organization.getOrganizationId(),organization.getOrganizationName(), organization.getAddress());
            facilityModels.add(facilityModel);
        }
        return facilityModels;

//        List<Facility> facilities = new ArrayList<Facility>();
//        log.info("getFacilityOfSalesman, salesmanId = " +
//                 salesmanId +
//                 ", ret userLoginFacilities.sz = " +
//                 userLoginFacilities.size());

//        for (UserLoginFacility uf : userLoginFacilities) {
//            Facility facility = facilityRepository.findByFacilityId(uf.getFacilityId());
//            log.info("getFacilityOfSalesman, facility in userLoginFacility " + uf.getFacilityId());
//        }
//        return facilities;
    }

    @Override
    public Facility createFacilityOfSalesman(String salesmanId, String facilityName, String address) {
        log.info("createFacilityOfSalesman, salesmanId = " +
                 salesmanId +
                 " facilityName = " +
                 facilityName +
                 ", address = " +
                 address);
        String facilityId = UUID.randomUUID().toString();
        Organization organization = new Organization(facilityId, facilityName, address);
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

    @Override
    public List<InventoryItem> findAllInventoryItemOfProductFromFacilityAndPositiveQuantityOnHand(
        String facilityId,
        String productId
    ) {
        return inventoryItemRepository.findAllByFacilityIdAndProductIdAndQuantityOnHandTotalGreaterThan(
            facilityId,
            productId,
            0);
    }

    @Override
    public Product createProduct(String productId, String productName) {
        Product product = productRepository.findByProductId(productId);
        if(product != null){
            log.info("createProduct -> ProductId " + productId + " EXISTS!!!!");
            return null;
        }
        product = new Product();
        product.setProductId(productId);
        product.setProductName(productName);
        productRepository.save(product);
        return product;
    }

    @Override
    public List<Product> findAllProducts() {
        List<Product> products = productRepository.findAll();
        return products;
    }

    @Override
    public void removeAllRunningData() {
        purchaseOrderRepository.deleteAll();
        inventoryItemRepository.deleteAll();
        productFacilityRepository.deleteAll();

    }


}

package com.hust.baseweb.applications.specialpurpose.saleslogmongo.service;

import com.hust.baseweb.applications.specialpurpose.saleslogmongo.common.UserLoginOrganizationRelationType;
import com.hust.baseweb.applications.specialpurpose.saleslogmongo.document.*;
import com.hust.baseweb.applications.specialpurpose.saleslogmongo.model.CreateSalesOrderInputModel;
import com.hust.baseweb.applications.specialpurpose.saleslogmongo.repository.*;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class SalesServiceImple implements SalesService {
    private final OrderItemRepository orderItemRepository;
    private final SalesOrderRepository salesOrderRepository;

    private final InventoryItemRepository inventoryItemRepository;
    private final InventoryItemDetailRepository inventoryItemDetailRepository;
    private final OrganizationRepository organizationRepository;
    private final CustomerRepository customerRepository;
    private final UserLoginCustomerRepository userLoginCustomerRepository;
    private final ProductFacilityRepository productFacilityRepository;

    @Override
    @Transactional
    public SalesOrder createSalesOrder(CreateSalesOrderInputModel input) {
        // TODO check product_facility to ensure that quantity on hand of each product is OK

        ModelMapper modelMapper = new ModelMapper();

        List<OrderItem> orderItems = input
            .getOrderItems()
            .stream()
            .map(orderItemModel -> modelMapper.map(orderItemModel, OrderItem.class))
            .collect(Collectors.toList());
        orderItems = orderItemRepository.saveAll(orderItems);

        SalesOrder salesOrder = input.toSalesOrder();
        salesOrder.setOrderItemIds(orderItems.stream().map(OrderItem::getOrderItemId).collect(Collectors.toList()));

        salesOrder = salesOrderRepository.save(salesOrder);

        /*
         * Xuất kho
         */

        //List<InventoryItem> inventoryItems = inventoryItemRepository.findAllByFacilityId(salesOrder.getFromFacilityId());
        //Map<String, InventoryItem> inventoryItemMap = inventoryItems
        //    .stream()
        //    .collect(Collectors.toMap(InventoryItem::getProductId, i -> i));

        List<InventoryItemDetail> inventoryItemDetails = new ArrayList<>();
        for (OrderItem orderItem : orderItems) {
            List<InventoryItem> inventoryItems = inventoryItemRepository.findAllByFacilityIdAndProductIdAndQuantityOnHandTotalGreaterThan(
                input.getFromFacilityId(), orderItem.getProductId(),0
            );
            // sort inventory items in an increasing order of quantityOnHand
            InventoryItem[] a = new InventoryItem[inventoryItems.size()];
            for(int i = 0; i < inventoryItems.size(); i++) a[i] = inventoryItems.get(i);
            for(int i = 0; i < a.length; i++){
                for(int j = i+1; j < a.length; j++){
                    if(a[i].getQuantityOnHandTotal() > a[j].getQuantityOnHandTotal()){
                        InventoryItem tmp = a[i]; a[i] = a[j]; a[j] = tmp;
                    }
                }
            }
            int remainQty = orderItem.getQuantity();
            for(int i = 0; i < a.length; i++) {
                int qtyDiff = 0;
                if (remainQty <= a[i].getQuantityOnHandTotal()) {
                    a[i].setQuantityOnHandTotal(a[i].getQuantityOnHandTotal() - remainQty);
                    inventoryItemRepository.save(a[i]);
                    qtyDiff = remainQty;
                    break;
                }else{
                    a[i].setQuantityOnHandTotal(0);
                    remainQty -= a[i].getQuantityOnHandTotal();
                    inventoryItemRepository.save(a[i]);
                    qtyDiff = a[i].getQuantityOnHandTotal();
                }
                InventoryItemDetail inventoryItemDetail = new InventoryItemDetail();
                inventoryItemDetail.setInventoryItemId(a[i].getInventoryItemId());
                inventoryItemDetail.setEffectiveDate(new Date());
                inventoryItemDetail.setQuantityOnHandDiff(qtyDiff);
                inventoryItemDetails.add(inventoryItemDetail);
            }

            // update to product_facility
            List<ProductFacility> productFacilities = productFacilityRepository.findAllByProductIdAndFacilityId(orderItem.getProductId(),input.getFromFacilityId());
            // TODO to be improved
            ProductFacility productFacility = productFacilities.get(0);
            productFacility.setQuantityOnHand(productFacility.getQuantityOnHand() - orderItem.getQuantity());
            productFacilityRepository.save(productFacility);

        }

        /*
        for (OrderItem orderItem : orderItems) {
            InventoryItem inventoryItem = inventoryItemMap.get(orderItem.getProductId());

            InventoryItemDetail inventoryItemDetail = new InventoryItemDetail();
            inventoryItemDetail.setInventoryItemId(inventoryItem.getInventoryItemId());
            inventoryItemDetail.setEffectiveDate(new Date());
            inventoryItemDetail.setQuantityOnHandDiff(orderItem.getQuantity());

            inventoryItemDetails.add(inventoryItemDetail);
        }
        */

        inventoryItemDetailRepository.saveAll(inventoryItemDetails);

        return salesOrder;
    }

    @Override
    public Customer createCusstomerOfSalesman(String salesmanId, String customerName, String address) {
        String customerId = UUID.randomUUID().toString();
        Organization organization = new Organization(customerId,customerName,address);
        organizationRepository.save(organization);
        Customer customer = new Customer(customerId);
        customerRepository.save(customer);
        UserLoginCustomer userLoginCustomer = new UserLoginCustomer();
        userLoginCustomer.setOrganizationId(customerId);
        userLoginCustomer.setUserLoginId(salesmanId);
        userLoginCustomer.setUserLoginOrganizationRelationType(UserLoginOrganizationRelationType.SALESMAN_SELL_TO_CUSTOMER);
        userLoginCustomer.setFromDate(new Date());
        userLoginCustomer.setThruDate(null);
        userLoginCustomerRepository.save(userLoginCustomer);
        return customer;
    }

    @Override
    public List<Customer> getCustomersOfSalesman(String salesmanId) {
        List<UserLoginCustomer> userLoginCustomers =
            userLoginCustomerRepository.findAllByUserLoginIdAndUserLoginOrganizationRelationTypeAndThruDate(salesmanId,
                                                                                                            UserLoginOrganizationRelationType.SALESMAN_SELL_TO_CUSTOMER, null);
        List<String> customerIds = userLoginCustomers.stream().map(UserLoginCustomer::getOrganizationId).
            distinct().collect(
            Collectors.toList());

        return customerRepository.findAllByCustomerIdIn(customerIds);
    }
}



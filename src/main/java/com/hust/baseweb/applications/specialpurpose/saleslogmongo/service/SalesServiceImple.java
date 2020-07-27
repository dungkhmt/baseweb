package com.hust.baseweb.applications.specialpurpose.saleslogmongo.service;

import com.hust.baseweb.applications.specialpurpose.saleslogmongo.common.UserLoginOrganizationRelationType;
import com.hust.baseweb.applications.specialpurpose.saleslogmongo.document.*;
import com.hust.baseweb.applications.specialpurpose.saleslogmongo.model.CreateSalesOrderInputModel;
import com.hust.baseweb.applications.specialpurpose.saleslogmongo.repository.*;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    @Override
    public SalesOrder createSalesOrder(CreateSalesOrderInputModel input) {
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

        List<InventoryItem> inventoryItems = inventoryItemRepository.findAllByFacilityId(salesOrder.getFromFacilityId());
        Map<String, InventoryItem> inventoryItemMap = inventoryItems
            .stream()
            .collect(Collectors.toMap(InventoryItem::getProductId, i -> i));

        List<InventoryItemDetail> inventoryItemDetails = new ArrayList<>();

        for (OrderItem orderItem : orderItems) {
            InventoryItem inventoryItem = inventoryItemMap.get(orderItem.getProductId());

            InventoryItemDetail inventoryItemDetail = new InventoryItemDetail();
            inventoryItemDetail.setInventoryItemId(inventoryItem.getInventoryItemId());
            inventoryItemDetail.setEffectiveDate(new Date());
            inventoryItemDetail.setQuantityOnHandDiff(orderItem.getQuantity());

            inventoryItemDetails.add(inventoryItemDetail);
        }

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



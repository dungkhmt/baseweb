package com.hust.baseweb.applications.specialpurpose.saleslogmongo.service;

import com.hust.baseweb.applications.specialpurpose.saleslogmongo.document.InventoryItem;
import com.hust.baseweb.applications.specialpurpose.saleslogmongo.document.InventoryItemDetail;
import com.hust.baseweb.applications.specialpurpose.saleslogmongo.document.OrderItem;
import com.hust.baseweb.applications.specialpurpose.saleslogmongo.document.SalesOrder;
import com.hust.baseweb.applications.specialpurpose.saleslogmongo.model.CreateSalesOrderInputModel;
import com.hust.baseweb.applications.specialpurpose.saleslogmongo.repository.InventoryItemDetailRepository;
import com.hust.baseweb.applications.specialpurpose.saleslogmongo.repository.InventoryItemRepository;
import com.hust.baseweb.applications.specialpurpose.saleslogmongo.repository.OrderItemRepository;
import com.hust.baseweb.applications.specialpurpose.saleslogmongo.repository.SalesOrderRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class SalesServiceImple implements SalesService {
    private final OrderItemRepository orderItemRepository;
    private final SalesOrderRepository salesOrderRepository;

    private final InventoryItemRepository inventoryItemRepository;
    private final InventoryItemDetailRepository inventoryItemDetailRepository;

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
}



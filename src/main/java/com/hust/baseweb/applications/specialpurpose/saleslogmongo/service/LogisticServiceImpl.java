package com.hust.baseweb.applications.specialpurpose.saleslogmongo.service;

import com.hust.baseweb.applications.specialpurpose.saleslogmongo.document.Facility;
import com.hust.baseweb.applications.specialpurpose.saleslogmongo.document.InventoryItem;
import com.hust.baseweb.applications.specialpurpose.saleslogmongo.document.InventoryItemDetail;
import com.hust.baseweb.applications.specialpurpose.saleslogmongo.document.Product;
import com.hust.baseweb.applications.specialpurpose.saleslogmongo.model.GetInventoryItemOutputModel;
import com.hust.baseweb.applications.specialpurpose.saleslogmongo.repository.FacilityRepository;
import com.hust.baseweb.applications.specialpurpose.saleslogmongo.repository.InventoryItemDetailRepository;
import com.hust.baseweb.applications.specialpurpose.saleslogmongo.repository.InventoryItemRepository;
import com.hust.baseweb.applications.specialpurpose.saleslogmongo.repository.ProductRepository;
import lombok.AllArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Hien Hoang (hienhoang2702@gmail.com)
 */
@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class LogisticServiceImpl implements LogisticService {

    private final FacilityRepository facilityRepository;
    private final InventoryItemRepository inventoryItemRepository;
    private final InventoryItemDetailRepository inventoryItemDetailRepository;
    private final ProductRepository productRepository;

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
}

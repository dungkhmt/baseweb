package com.hust.baseweb.applications.specialpurpose.saleslogmongo.repository;

import com.hust.baseweb.applications.specialpurpose.saleslogmongo.document.InventoryItem;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * @author Hien Hoang (hienhoang2702@gmail.com)
 */
public interface InventoryItemRepository extends MongoRepository<InventoryItem, ObjectId> {

    List<InventoryItem> findAllByFacilityId(String facilityId);

    List<InventoryItem> findAllByFacilityIdAndProductIdAndQuantityOnHandTotalGreaterThan(String facilityId, String productId, int quantityOnHand);
}

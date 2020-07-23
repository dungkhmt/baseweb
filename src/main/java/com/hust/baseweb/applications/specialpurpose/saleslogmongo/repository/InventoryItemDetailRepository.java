package com.hust.baseweb.applications.specialpurpose.saleslogmongo.repository;

import com.hust.baseweb.applications.specialpurpose.saleslogmongo.document.InventoryItemDetail;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Collection;
import java.util.List;

/**
 * @author Hien Hoang (hienhoang2702@gmail.com)
 */
public interface InventoryItemDetailRepository extends MongoRepository<InventoryItemDetail, ObjectId> {

    List<InventoryItemDetail> findAllByInventoryItemIdIn(Collection<ObjectId> inventoryItemId);
}

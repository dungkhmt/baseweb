package com.hust.baseweb.applications.order.repo.mongodb;

import com.hust.baseweb.applications.order.document.OrderHeaderRemoved;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author Hien Hoang (hienhoang2702@gmail.com)
 */
public interface OrderHeaderRemovedRepo extends MongoRepository<OrderHeaderRemoved, ObjectId> {

}

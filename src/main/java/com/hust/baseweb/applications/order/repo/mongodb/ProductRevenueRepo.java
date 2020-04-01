package com.hust.baseweb.applications.order.repo.mongodb;

import com.hust.baseweb.applications.order.document.aggregation.ProductRevenue;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * @author Hien Hoang (hienhoang2702@gmail.com)
 */
public interface ProductRevenueRepo extends MongoRepository<ProductRevenue, ProductRevenue.ProductRevenueId> {
    List<ProductRevenue> findAllByIdIn(List<ProductRevenue.ProductRevenueId> ids);
}

package com.hust.baseweb.applications.order.repo.mongodb;

import com.hust.baseweb.applications.order.document.aggregation.CustomerRevenue;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * @author Hien Hoang (hienhoang2702@gmail.com)
 */
public interface CustomerRevenueRepo extends MongoRepository<CustomerRevenue, CustomerRevenue.CustomerRevenueId> {
    List<CustomerRevenue> findAllByIdIn(List<CustomerRevenue.CustomerRevenueId> ids);
}

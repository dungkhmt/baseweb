package com.hust.baseweb.applications.order.repo.mongodb;

import com.hust.baseweb.applications.order.document.aggregation.TotalRevenue;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDate;

/**
 * @author Hien Hoang (hienhoang2702@gmail.com)
 */
public interface TotalRevenueRepo extends MongoRepository<TotalRevenue, LocalDate> {
}

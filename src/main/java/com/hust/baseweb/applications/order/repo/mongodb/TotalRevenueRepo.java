package com.hust.baseweb.applications.order.repo.mongodb;

import com.hust.baseweb.applications.order.document.aggregation.TotalRevenue;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDate;
import java.util.List;

/**
 * @author Hien Hoang (hienhoang2702@gmail.com)
 */
public interface TotalRevenueRepo extends MongoRepository<TotalRevenue, LocalDate> {
    List<TotalRevenue> findAllByIdIn(List<LocalDate> ids);
    List<TotalRevenue> findAllByIdBetween(LocalDate fromDate, LocalDate toDate);
}

package com.hust.baseweb.applications.order.repo.mongodb;

import com.hust.baseweb.applications.order.document.aggregation.CustomerRevenue;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

/**
 * @author Hien Hoang (hienhoang2702@gmail.com)
 */
public interface CustomerRevenueRepo extends MongoRepository<CustomerRevenue, CustomerRevenue.Id> {

    List<CustomerRevenue> findAllByIdIn(List<CustomerRevenue.Id> ids);

    List<CustomerRevenue> findAllById_CustomerIdAndId_DateBetween(UUID customerId, LocalDate from, LocalDate to);
}

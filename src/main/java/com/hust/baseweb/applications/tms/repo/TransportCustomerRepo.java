package com.hust.baseweb.applications.tms.repo;

import com.hust.baseweb.applications.tms.document.aggregation.TransportCustomer;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

/**
 * @author Hien Hoang (hienhoang2702@gmail.com)
 */
public interface TransportCustomerRepo extends MongoRepository<TransportCustomer, TransportCustomer.Id> {

    List<TransportCustomer> findAllByIdIn(List<TransportCustomer.Id> ids);

    List<TransportCustomer> findAllById_CustomerIdAndId_DateBetween(UUID customerId, LocalDate from, LocalDate to);
}

package com.hust.baseweb.applications.tms.repo;

import com.hust.baseweb.applications.tms.document.aggregation.TransportCustomer;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * @author Hien Hoang (hienhoang2702@gmail.com)
 */
public interface TransportCustomerRepo extends MongoRepository<TransportCustomer, TransportCustomer.Id> {
    List<TransportCustomer> findAllByIdIn(List<TransportCustomer.Id> ids);
}

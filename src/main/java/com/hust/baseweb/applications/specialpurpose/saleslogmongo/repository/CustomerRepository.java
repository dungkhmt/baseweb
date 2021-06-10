package com.hust.baseweb.applications.specialpurpose.saleslogmongo.repository;

import com.hust.baseweb.applications.specialpurpose.saleslogmongo.document.Customer;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Collection;
import java.util.List;

/**
 * @author Hien Hoang (hienhoang2702@gmail.com)
 */
public interface CustomerRepository extends MongoRepository<Customer, String> {

    Customer findByCustomerId(String customerId);

    List<Customer> findAllByCustomerIdIn(Collection<String> customerIds);
}

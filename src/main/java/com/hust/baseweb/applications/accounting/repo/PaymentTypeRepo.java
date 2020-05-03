package com.hust.baseweb.applications.accounting.repo;

import com.hust.baseweb.applications.accounting.document.PaymentType;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author Hien Hoang (hienhoang2702@gmail.com)
 */
public interface PaymentTypeRepo extends MongoRepository<PaymentType, String> {
}

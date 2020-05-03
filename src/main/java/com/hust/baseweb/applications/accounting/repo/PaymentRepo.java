package com.hust.baseweb.applications.accounting.repo;

import com.hust.baseweb.applications.accounting.document.Payment;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author Hien Hoang (hienhoang2702@gmail.com)
 */
public interface PaymentRepo extends MongoRepository<Payment, String> {
}

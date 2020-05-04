package com.hust.baseweb.applications.accounting.repo;

import com.hust.baseweb.applications.accounting.document.PaymentApplication;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.UUID;

/**
 * @author Hien Hoang (hienhoang2702@gmail.com)
 */
public interface PaymentApplicationRepo extends MongoRepository<PaymentApplication, UUID> {
}

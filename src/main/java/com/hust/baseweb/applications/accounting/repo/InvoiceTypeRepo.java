package com.hust.baseweb.applications.accounting.repo;

import com.hust.baseweb.applications.accounting.document.InvoiceType;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author Hien Hoang (hienhoang2702@gmail.com)
 */
public interface InvoiceTypeRepo extends MongoRepository<InvoiceType, String> {
}

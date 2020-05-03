package com.hust.baseweb.applications.accounting.repo;

import com.hust.baseweb.applications.accounting.document.InvoiceItemType;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author Hien Hoang (hienhoang2702@gmail.com)
 */
public interface InvoiceItemTypeRepo extends MongoRepository<InvoiceItemType, String> {
}

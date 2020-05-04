package com.hust.baseweb.applications.accounting.repo;

import com.hust.baseweb.applications.accounting.document.InvoiceItem;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author Hien Hoang (hienhoang2702@gmail.com)
 */
public interface InvoiceItemRepo extends MongoRepository<InvoiceItem, InvoiceItem.Id> {
}

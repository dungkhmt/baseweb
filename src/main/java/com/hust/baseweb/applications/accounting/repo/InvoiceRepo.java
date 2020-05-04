package com.hust.baseweb.applications.accounting.repo;

import com.hust.baseweb.applications.accounting.document.Invoice;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author Hien Hoang (hienhoang2702@gmail.com)
 */
public interface InvoiceRepo extends MongoRepository<Invoice, String> {
}

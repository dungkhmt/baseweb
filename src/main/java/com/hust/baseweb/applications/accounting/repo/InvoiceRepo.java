package com.hust.baseweb.applications.accounting.repo;

import com.hust.baseweb.applications.accounting.document.Invoice;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.UUID;

/**
 * @author Hien Hoang (hienhoang2702@gmail.com)
 */
public interface InvoiceRepo extends MongoRepository<Invoice, String> {
    @Query("{ $expr: { $ne: [ \"$amount\" , \"$paidAmount\" ] } }")
    List<Invoice> findAllByAmountNotEqualWithPaidAmount();

    List<Invoice> findAllByToPartyCustomerId(UUID partyCustomerId);
}

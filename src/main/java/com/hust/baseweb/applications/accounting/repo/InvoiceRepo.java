package com.hust.baseweb.applications.accounting.repo;

import com.hust.baseweb.applications.accounting.document.Invoice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.UUID;

/**
 * @author Hien Hoang (hienhoang2702@gmail.com)
 */
public interface InvoiceRepo extends MongoRepository<Invoice, String> {

    @Query("{\n" +
           "    $and: [\n" +
           "        { _id: { $regex:?0 }, toPartyCustomerId:?1},\n" +
           "        { $expr: { $ne: [\"$amount\", \"$paidAmount\"] } }\n" +
           "    ]\n" +
           "}")
    Page<Invoice> findAllByInvoiceIdAndToPartyCustomerIdAndAmountNotEqualWithPaidAmount(
        String invoiceId,
        UUID toPartyCustomerId,
        Pageable pageable);

    @Query("{\n" +
           "    $and: [\n" +
           "        { _id: { $regex:?0 }},\n" +
           "        { $expr: { $ne: [\"$amount\", \"$paidAmount\"] } }\n" +
           "    ]\n" +
           "}")
    Page<Invoice> findAllByInvoiceIdAndAmountNotEqualWithPaidAmount(
        String invoiceId,
        Pageable pageable);

    @Query("{\n" +
           "    $and: [\n" +
           "        { toPartyCustomerId:?0},\n" +
           "        { $expr: { $ne: [\"$amount\", \"$paidAmount\"] } }\n" +
           "    ]\n" +
           "}")
    Page<Invoice> findAllByToPartyCustomerIdAndAmountNotEqualWithPaidAmount(
        UUID toPartyCustomerId,
        Pageable pageable);

    @Query("{ $expr: { $ne: [ \"$amount\" , \"$paidAmount\" ] } }")
    Page<Invoice> findAllByAmountNotEqualWithPaidAmount(Pageable pageable);

    @Query("{ $expr: { $ne: [ \"$amount\" , \"$paidAmount\" ] } }")
    List<Invoice> findAllByAmountNotEqualWithPaidAmount();

    List<Invoice> findAllByToPartyCustomerId(UUID partyCustomerId);
}

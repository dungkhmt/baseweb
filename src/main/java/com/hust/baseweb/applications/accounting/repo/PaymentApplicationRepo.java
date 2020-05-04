package com.hust.baseweb.applications.accounting.repo;

import com.hust.baseweb.applications.accounting.document.PaymentApplication;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.UUID;

/**
 * @author Hien Hoang (hienhoang2702@gmail.com)
 */
public interface PaymentApplicationRepo extends MongoRepository<PaymentApplication, UUID> {

    List<PaymentApplication> findAllByInvoiceId(String invoiceId);

    List<PaymentApplication> findAllByPaymentId(String paymentId);

}

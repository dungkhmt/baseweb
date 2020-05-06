package com.hust.baseweb.applications.accounting.service;

import com.hust.baseweb.applications.accounting.document.Payment;

import java.util.List;

/**
 * @author Hien Hoang (hienhoang2702@gmail.com)
 */
public interface PaymentService {

    Payment.Model createPayment(Payment.CreateModel paymentCreateModel);

    Payment.Model getPayment(String paymentId);

    List<Payment.Model> getAllPayment();

    List<Payment.Model> getAllByInvoiceId(String invoiceId);

    Payment save(Payment payment);

    List<Payment> saveAll(List<Payment> payments);
}

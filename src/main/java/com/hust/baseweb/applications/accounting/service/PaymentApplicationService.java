package com.hust.baseweb.applications.accounting.service;

import com.hust.baseweb.applications.accounting.document.Payment;
import com.hust.baseweb.applications.accounting.document.PaymentApplication;

import java.util.List;

/**
 * @author Hien Hoang (hienhoang2702@gmail.com)
 */
public interface PaymentApplicationService {
    List<PaymentApplication.Model> findAllByInvoiceId(String invoiceId);

    Payment.ApplicationModel findAllByPaymentId(String paymentId);
}

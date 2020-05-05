package com.hust.baseweb.applications.accounting.service;

import com.hust.baseweb.applications.accounting.document.Payment;
import com.hust.baseweb.applications.accounting.document.PaymentApplication;
import com.hust.baseweb.applications.accounting.repo.PaymentApplicationRepo;
import com.hust.baseweb.applications.accounting.repo.PaymentRepo;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Hien Hoang (hienhoang2702@gmail.com)
 */
@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
@Transactional
public class PaymentServiceImpl implements PaymentService {

    private PaymentRepo paymentRepo;
    private PaymentApplicationRepo paymentApplicationRepo;

    @Override
    public List<Payment.Model> getAllPayment() {
        return paymentRepo.findAll().stream().map(Payment::toModel).collect(Collectors.toList());
    }

    @Override
    public List<Payment.Model> getAllByInvoiceId(String invoiceId) {
        List<PaymentApplication> paymentApplications = paymentApplicationRepo.findAllByInvoiceId(invoiceId);
        List<String> paymentIds = paymentApplications.stream()
                .map(PaymentApplication::getPaymentId)
                .distinct()
                .collect(Collectors.toList());
        List<Payment> payments = paymentRepo.findAllByPaymentIdIn(paymentIds);
        return payments.stream().map(Payment::toModel).collect(Collectors.toList());
    }
}

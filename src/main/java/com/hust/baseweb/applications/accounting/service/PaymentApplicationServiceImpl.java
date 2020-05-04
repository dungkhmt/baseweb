package com.hust.baseweb.applications.accounting.service;

import com.hust.baseweb.applications.accounting.document.Payment;
import com.hust.baseweb.applications.accounting.document.PaymentApplication;
import com.hust.baseweb.applications.accounting.repo.PaymentApplicationRepo;
import com.hust.baseweb.applications.accounting.repo.PaymentRepo;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

/**
 * @author Hien Hoang (hienhoang2702@gmail.com)
 */
@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class PaymentApplicationServiceImpl implements PaymentApplicationService {

    private PaymentApplicationRepo paymentApplicationRepo;
    private PaymentRepo paymentRepo;

    @Override
    public List<PaymentApplication.Model> findAllByInvoiceId(String invoiceId) {
        return paymentApplicationRepo.findAllByInvoiceId(invoiceId)
                .stream()
                .map(PaymentApplication::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public Payment.ApplicationModel findAllByPaymentId(String paymentId) {
        Payment.Model paymentModel = paymentRepo.findById(paymentId).orElseThrow(NoSuchElementException::new).toModel();
        List<PaymentApplication.Model> paymentApplicationModels = paymentApplicationRepo.findAllByPaymentId(paymentId)
                .stream()
                .map(PaymentApplication::toModel)
                .collect(Collectors.toList());
        return new Payment.ApplicationModel(paymentModel, paymentApplicationModels);
    }
}

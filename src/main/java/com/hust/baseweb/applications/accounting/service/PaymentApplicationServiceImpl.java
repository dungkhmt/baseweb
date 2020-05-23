package com.hust.baseweb.applications.accounting.service;

import com.hust.baseweb.applications.accounting.document.Invoice;
import com.hust.baseweb.applications.accounting.document.Payment;
import com.hust.baseweb.applications.accounting.document.PaymentApplication;
import com.hust.baseweb.applications.accounting.repo.InvoiceRepo;
import com.hust.baseweb.applications.accounting.repo.PaymentApplicationRepo;
import com.hust.baseweb.applications.accounting.repo.PaymentRepo;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

/**
 * @author Hien Hoang (hienhoang2702@gmail.com)
 */
@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
@Transactional
public class PaymentApplicationServiceImpl implements PaymentApplicationService {

    private PaymentApplicationRepo paymentApplicationRepo;
    private PaymentRepo paymentRepo;
    private InvoiceRepo invoiceRepo;

    private PaymentService paymentService;

    @Override
    public List<PaymentApplication.Model> findAllByInvoiceId(String invoiceId) {
        return paymentApplicationRepo.findAllByInvoiceId(invoiceId)
            .stream()
            .map(PaymentApplication::toModel)
            .collect(Collectors.toList());
    }

    @Override
    public List<PaymentApplication.Model> findAllByPaymentId(String paymentId) {
        return paymentApplicationRepo.findAllByPaymentId(paymentId)
            .stream()
            .map(PaymentApplication::toModel)
            .collect(Collectors.toList());
    }

    @Override
    public PaymentApplication.Model createPaymentApplication(PaymentApplication.CreateModel paymentApplicationCreateModel) {
        Date now = new Date();
        PaymentApplication paymentApplication = new PaymentApplication(
            null,
            paymentApplicationCreateModel.getPaymentId(),
            paymentApplicationCreateModel.getInvoiceId(),
            paymentApplicationCreateModel.getAmount(),
            "CUR_vnd",
            now,
            now,
            now
        );
        paymentApplication = paymentApplicationRepo.save(paymentApplication);
        Invoice invoice = invoiceRepo.findById(paymentApplication.getInvoiceId())
            .orElseThrow(NoSuchElementException::new);
        Payment payment = paymentRepo.findById(paymentApplication.getPaymentId())
            .orElseThrow(NoSuchElementException::new);
        invoice.setPaidAmount(invoice.getPaidAmount() + paymentApplication.getAppliedAmount());
        payment.setAppliedAmount(payment.getAppliedAmount() + paymentApplication.getAppliedAmount());

        invoiceRepo.save(invoice);
        paymentRepo.save(payment);

        return paymentApplication.toModel();
    }

    @Override
    public PaymentApplication.Model quickCreatePaymentApplication(PaymentApplication.CreateModel paymentApplicationCreateModel) {
        Invoice invoice = invoiceRepo.findById(paymentApplicationCreateModel.getInvoiceId())
            .orElseThrow(NoSuchElementException::new);
        Payment.Model paymentModel = paymentService.createPayment(new Payment.CreateModel(invoice.getToPartyCustomerId()
            .toString(),
            paymentApplicationCreateModel.getAmount()));
        return createPaymentApplication(new PaymentApplication.CreateModel(paymentModel.getPaymentId(),
            invoice.getInvoiceId(),
            Math.min(invoice.getAmount() - invoice.getPaidAmount(), paymentApplicationCreateModel.getAmount())));
    }
}

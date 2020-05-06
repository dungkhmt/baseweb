package com.hust.baseweb.applications.accounting.controller;

import com.hust.baseweb.applications.accounting.document.Invoice;
import com.hust.baseweb.applications.accounting.document.InvoiceItem;
import com.hust.baseweb.applications.accounting.document.Payment;
import com.hust.baseweb.applications.accounting.service.InvoiceItemService;
import com.hust.baseweb.applications.accounting.service.InvoiceService;
import com.hust.baseweb.applications.accounting.service.PaymentApplicationService;
import com.hust.baseweb.applications.accounting.service.PaymentService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Hien Hoang (hienhoang2702@gmail.com)
 */
@RestController
@CrossOrigin
@Log4j2
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class AccountingController {
    private PaymentService paymentService;
    private InvoiceService invoiceService;
    private InvoiceItemService invoiceItemService;
    private PaymentApplicationService paymentApplicationService;

    @GetMapping("/get-all-payment")
    public ResponseEntity<List<Payment.Model>> getAllPayment() {
        return ResponseEntity.ok(paymentService.getAllPayment());
    }

    @GetMapping("/get-all-invoice")
    public ResponseEntity<List<Invoice.Model>> getAllInvoice() {
        return ResponseEntity.ok(invoiceService.getAllInvoice());
    }

    @GetMapping("/get-invoice-by-id/{invoiceId}")
    public ResponseEntity<Invoice.Model> getInvoiceById(@PathVariable String invoiceId) {
        return ResponseEntity.ok(invoiceService.getInvoice(invoiceId));
    }

    @GetMapping("/get-payment-by-id/{paymentId}")
    public ResponseEntity<Payment.Model> getPaymentById(@PathVariable String paymentId) {
        return ResponseEntity.ok(paymentService.getPayment(paymentId));
    }

    @PostMapping("/create-payment")
    public ResponseEntity<Payment.Model> createPayment(@RequestBody Payment.CreateModel paymentCreateModel) {
        return ResponseEntity.ok(paymentService.createPayment(paymentCreateModel));
    }

    @GetMapping("/get-all-invoice-item-by-invoice-id/{invoiceId}")
    public ResponseEntity<List<InvoiceItem.Model>> getAllInvoiceItemByInvoiceId(@PathVariable String invoiceId) {
        return ResponseEntity.ok(invoiceItemService.findByInvoiceId(invoiceId));
    }

    @GetMapping("/get-all-payment-by-invoice-id/{invoiceId}")
    public ResponseEntity<List<Payment.Model>> getAllPaymentByInvoiceId(@PathVariable String invoiceId) {
        return ResponseEntity.ok(paymentService.getAllByInvoiceId(invoiceId));
    }

    @GetMapping("/get-payment-detail-by-payment-id/{paymentId}")
    public ResponseEntity<Payment.ApplicationModel> getPaymentDetailByPaymentId(@PathVariable String paymentId) {
        return ResponseEntity.ok(paymentApplicationService.findAllByPaymentId(paymentId));
    }

    @GetMapping("/get-all-unpaid-invoices")
    public ResponseEntity<List<Invoice.Model>> getAllUnpaidInvoices() {
        return ResponseEntity.ok(invoiceService.getAllUnpaidInvoices());
    }
}

package com.hust.baseweb.applications.accounting.controller;

import com.hust.baseweb.applications.accounting.document.Invoice;
import com.hust.baseweb.applications.accounting.document.InvoiceItem;
import com.hust.baseweb.applications.accounting.document.Payment;
import com.hust.baseweb.applications.accounting.document.PaymentApplication;
import com.hust.baseweb.applications.accounting.service.InvoiceItemService;
import com.hust.baseweb.applications.accounting.service.InvoiceService;
import com.hust.baseweb.applications.accounting.service.PaymentApplicationService;
import com.hust.baseweb.applications.accounting.service.PaymentService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    @GetMapping("/get-unpaid-invoices-group-by-distributor-id")
    public ResponseEntity<List<Invoice.DistributorUnpaidModel>> getUnpaidInvoicesGroupByDistributorId() {

        return ResponseEntity.ok(invoiceService.getAllUnpaidInvoiceGroupByDistributor());
    }

    @GetMapping("/get-unpaid-invoice-by-distributor-id/{distributorId}")
    public ResponseEntity<Invoice.DistributorUnpaidModel> getUnpaidInvoiceByDistributorId(
        @PathVariable String distributorId) {

        return ResponseEntity.ok(invoiceService.getUnpaidInvoiceByDistributor(distributorId));
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

    @GetMapping("/get-all-payment-application-by-invoice-id/{invoiceId}")
    public ResponseEntity<List<PaymentApplication.Model>> getAllPaymentApplicationByInvoiceId(
        @PathVariable String invoiceId) {

        return ResponseEntity.ok(paymentApplicationService.findAllByInvoiceId(invoiceId));
    }

    @GetMapping("/get-payment-application-by-payment-id/{paymentId}")
    public ResponseEntity<List<PaymentApplication.Model>> getPaymentDetailByPaymentId(@PathVariable String paymentId) {

        return ResponseEntity.ok(paymentApplicationService.findAllByPaymentId(paymentId));
    }

    @GetMapping("/get-all-unpaid-invoices")
    public ResponseEntity<List<Invoice.Model>> getAllUnpaidInvoices() {

        return ResponseEntity.ok(invoiceService.getAllUnpaidInvoices());
    }

    @GetMapping("/get-page-unpaid-invoices")
    public ResponseEntity<Page<Invoice.Model>> getPageUnpaidInvoices(
        @RequestParam(value = "filtering", required = false) String filtering,
        Pageable pageable) {

        if (filtering == null) {
            return ResponseEntity.ok(invoiceService.getPageUnpaidInvoices(pageable));
        } else {
            Map<String, String> filterMap = Arrays
                .stream(filtering.split(","))
                .map(s -> s.split(":"))
                .collect(Collectors.toMap(ss -> ss[0], ss -> ss[1]));
            return ResponseEntity.ok(invoiceService.getPageUnpaidInvoices(
                filterMap.get("invoiceId"),
                filterMap.get("toPartyCustomerId"),
                pageable));
        }
    }

    @PostMapping("/create-payment-application")
    public ResponseEntity<PaymentApplication.Model> createPaymentApplication(
        @RequestBody PaymentApplication.CreateModel paymentApplicationCreateModel) {

        if (paymentApplicationCreateModel.getPaymentId() != null) {
            return ResponseEntity.ok(paymentApplicationService.createPaymentApplication(paymentApplicationCreateModel));
        } else {
            return ResponseEntity.ok(paymentApplicationService.quickCreatePaymentApplication(
                paymentApplicationCreateModel));
        }
    }
}

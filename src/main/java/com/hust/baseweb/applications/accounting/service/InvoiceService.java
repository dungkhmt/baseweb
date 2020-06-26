package com.hust.baseweb.applications.accounting.service;

import com.hust.baseweb.applications.accounting.document.Invoice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * @author Hien Hoang (hienhoang2702@gmail.com)
 */
public interface InvoiceService {

    List<Invoice.Model> getAllInvoice();

    List<Invoice.Model> getAllUnpaidInvoices();

    Page<Invoice.Model> getPageUnpaidInvoices(Pageable pageable);

    Page<Invoice.Model> getPageUnpaidInvoices(
        String invoiceId,
        String toPartyCustomerName,
        Pageable pageable
    );

    List<Invoice.DistributorUnpaidModel> getAllUnpaidInvoiceGroupByDistributor();

    Invoice.DistributorUnpaidModel getUnpaidInvoiceByDistributor(String distributorId);

    Invoice.Model getInvoice(String invoiceId);

    Invoice save(Invoice invoice);

    List<Invoice> saveAll(List<Invoice> invoices);
}

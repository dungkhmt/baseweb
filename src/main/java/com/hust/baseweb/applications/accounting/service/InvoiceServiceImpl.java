package com.hust.baseweb.applications.accounting.service;

import com.hust.baseweb.applications.accounting.document.Invoice;
import com.hust.baseweb.applications.accounting.entity.InvoiceSequenceId;
import com.hust.baseweb.applications.accounting.repo.InvoiceRepo;
import com.hust.baseweb.applications.accounting.repo.sequenceid.InvoiceSequenceIdRepo;
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
@org.springframework.transaction.annotation.Transactional
@javax.transaction.Transactional
public class InvoiceServiceImpl implements InvoiceService {

    private InvoiceRepo invoiceRepo;
    private InvoiceSequenceIdRepo invoiceSequenceIdRepo;

    @Override
    public List<Invoice.Model> getAllInvoice() {
        return invoiceRepo.findAll().stream().map(Invoice::toModel).collect(Collectors.toList());
    }

    @Override
    public Invoice.Model getInvoice(String invoiceId) {
        return invoiceRepo.findById(invoiceId).orElseThrow(NoSuchElementException::new).toModel();
    }

    @Override
    public Invoice save(Invoice invoice) {
        InvoiceSequenceId id = invoiceSequenceIdRepo.save(new InvoiceSequenceId());
        invoice.setInvoiceId(Invoice.convertSequenceIdToInvoiceId(id.getId()));
        return invoiceRepo.save(invoice);
    }

    @Override
    public List<Invoice> saveAll(List<Invoice> invoices) {
        List<InvoiceSequenceId> ids = invoiceSequenceIdRepo.saveAll(invoices.stream()
                .map(invoice -> new InvoiceSequenceId())
                .collect(Collectors.toList()));
        for (int i = 0; i < invoices.size(); i++) {
            invoices.get(i).setInvoiceId(Invoice.convertSequenceIdToInvoiceId(ids.get(i).getId()));
        }
        return invoiceRepo.saveAll(invoices);
    }

}

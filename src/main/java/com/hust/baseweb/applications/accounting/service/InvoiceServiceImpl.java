package com.hust.baseweb.applications.accounting.service;

import com.hust.baseweb.applications.accounting.document.Invoice;
import com.hust.baseweb.applications.accounting.entity.InvoiceSequenceId;
import com.hust.baseweb.applications.accounting.repo.InvoiceRepo;
import com.hust.baseweb.applications.accounting.repo.sequenceid.InvoiceSequenceIdRepo;
import com.hust.baseweb.applications.customer.entity.PartyDistributor;
import com.hust.baseweb.applications.order.repo.PartyDistributorRepo;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
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
    private PartyDistributorRepo partyDistributorRepo;

    @Override
    public List<Invoice.Model> getAllInvoice() {
        return invoiceRepo.findAll().stream().map(Invoice::toModel).collect(Collectors.toList());
    }

    @Override
    public List<Invoice.Model> getAllUnpaidInvoices() {
        return invoiceRepo.findAllByAmountNotEqualWithPaidAmount()
                .stream()
                .map(Invoice::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public List<Invoice.DistributorUnpaidModel> getAllUnpaidInvoiceGroupByDistributor() {
        List<Invoice> unpaidInvoices = invoiceRepo.findAllByAmountNotEqualWithPaidAmount();
        List<UUID> partyDistributorIds = unpaidInvoices.stream()
                .map(Invoice::getToPartyCustomerId)
                .distinct()
                .collect(Collectors.toList());
        Map<UUID, PartyDistributor> partyDistributorMap = partyDistributorRepo.findAllByPartyIdIn(partyDistributorIds)
                .stream()
                .collect(Collectors.toMap(PartyDistributor::getPartyId, p -> p));

        unpaidInvoices = unpaidInvoices.stream()
                .filter(invoice -> partyDistributorMap.containsKey(invoice.getToPartyCustomerId()))
                .collect(Collectors.toList()); // lọc các invoice thuộc về khách hàng là distributor
        return Invoice.toUnpaidDistributorModels(unpaidInvoices, partyDistributorMap);
    }

    @Override
    public Invoice.DistributorUnpaidModel getUnpaidInvoiceByDistributor(String distributorId) {
        PartyDistributor partyDistributor = partyDistributorRepo.findById(UUID.fromString(distributorId))
                .orElseThrow(NoSuchElementException::new);
        List<Invoice> unpaidInvoices = invoiceRepo.findAllByToPartyCustomerId(UUID.fromString(distributorId))
                .stream()
                .filter(invoice -> invoice.getAmount() > invoice.getPaidAmount())
                .collect(Collectors.toList());
        Invoice.DistributorUnpaidModel distributorUnpaidModel = new Invoice.DistributorUnpaidModel(
                partyDistributor.getPartyId().toString(),
                partyDistributor.getDistributorCode(),
                partyDistributor.getDistributorName(),
                0.0,
                new ArrayList<>());
        for (Invoice unpaidInvoice : unpaidInvoices) {
            distributorUnpaidModel.append(unpaidInvoice);
        }
        return distributorUnpaidModel;
    }

    @Override
    public Invoice.Model getInvoice(String invoiceId) {
        Invoice invoice = invoiceRepo.findById(invoiceId).orElseThrow(NoSuchElementException::new);
        PartyDistributor partyDistributor = partyDistributorRepo.findById(invoice.getToPartyCustomerId())
                .orElse(new PartyDistributor());
        return invoice.toModel(partyDistributor.getDistributorName());
    }

    @Override
    public Invoice save(Invoice invoice) {
        if (invoice.getInvoiceId() == null) {
            InvoiceSequenceId id = invoiceSequenceIdRepo.save(new InvoiceSequenceId());
            invoice.setInvoiceId(Invoice.convertSequenceIdToInvoiceId(id.getId()));
        }
        return invoiceRepo.save(invoice);
    }

    @Override
    public List<Invoice> saveAll(List<Invoice> invoices) {
        List<Invoice> newInvoices = invoices.stream()
                .filter(invoice -> invoice.getInvoiceId() == null).collect(Collectors.toList());
        if (!newInvoices.isEmpty()) {
            List<InvoiceSequenceId> ids = invoiceSequenceIdRepo.saveAll(newInvoices.stream()
                    .map(invoice -> new InvoiceSequenceId())
                    .collect(Collectors.toList()));
            for (int i = 0; i < newInvoices.size(); i++) {
                invoices.get(i).setInvoiceId(Invoice.convertSequenceIdToInvoiceId(ids.get(i).getId()));
            }
        }
        return invoiceRepo.saveAll(invoices);
    }

}

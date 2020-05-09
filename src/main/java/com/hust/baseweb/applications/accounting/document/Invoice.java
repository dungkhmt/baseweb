package com.hust.baseweb.applications.accounting.document;

import com.hust.baseweb.applications.customer.entity.PartyDistributor;
import com.hust.baseweb.utils.Constant;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Hien Hoang (hienhoang2702@gmail.com)
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Document
public class Invoice {

    @Id
    private String invoiceId;           // varchar(60),
    private InvoiceType invoiceType;      // varchar(60),
    private StatusItem statusId;            // varchar(60),
    private Date invoiceDate;         // TIMESTAMP,
    private UUID toPartyCustomerId; // uuid,
    private UUID fromVendorId;       // uuid,
    private Double amount;               // decimal(18, 2),
    private Double paidAmount;
    private String currencyUomId;      // varchar(60),
    private Date lastUpdatedStamp;   // TIMESTAMP,
    private Date createdStamp;        // TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    public static String convertSequenceIdToInvoiceId(Long id) {
        return "INV" + String.format("%010d", id);
    }

    public Model toModel() {
        return new Model(
                invoiceId,
                invoiceType.toString(),
                statusId.toString(),
                Constant.DATE_FORMAT.format(invoiceDate),
                Optional.ofNullable(toPartyCustomerId).map(UUID::toString).orElse(null),
                Optional.ofNullable(fromVendorId).map(UUID::toString).orElse(null),
                amount,
                paidAmount,
                currencyUomId
        );
    }

    public static List<DistributorUnpaidModel> toUnpaidDistributorModels(List<Invoice> invoices,
                                                                         Map<UUID, PartyDistributor> partyDistributorMap) {
        Map<UUID, DistributorUnpaidModel> customerCodeToByDistributorModel = new HashMap<>();
        for (Invoice invoice : invoices) {
            customerCodeToByDistributorModel.computeIfAbsent(invoice.getToPartyCustomerId(),
                    partyCustomerId -> {
                        PartyDistributor partyDistributor = partyDistributorMap.get(partyCustomerId);
                        return new DistributorUnpaidModel(partyDistributor.getDistributorCode(),
                                partyDistributor.getDistributorName(), 0.0, new ArrayList<>());
                    }).append(invoice);
        }
        return customerCodeToByDistributorModel.values()
                .stream()
                .filter(distributorUnpaidModel -> distributorUnpaidModel.getTotalUnpaid() > 0)
                .collect(Collectors.toList());
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    public static class Model {
        private String invoiceId;           // varchar(60),
        private String invoiceType;      // varchar(60),
        private String statusId;            // varchar(60),
        private String invoiceDate;         // TIMESTAMP,
        private String toPartyCustomerId; // uuid,
        private String fromVendorId;       // uuid,
        private Double amount;               // decimal(18, 2),
        private Double paidAmount;               // decimal(18, 2),
        private String currencyUomId;      // varchar(60),
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    public static class DistributorUnpaidModel {
        private String distributorCode;
        private String distributorName;
        private Double totalUnpaid;

        private List<Invoice.Model> unpaidInvoices;

        public void append(Invoice invoice) {
            totalUnpaid += invoice.getAmount() - invoice.getPaidAmount();
            unpaidInvoices.add(invoice.toModel());
        }
    }
}

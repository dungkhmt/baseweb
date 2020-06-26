package com.hust.baseweb.applications.order.model;

import com.hust.baseweb.applications.accounting.document.InvoiceType;
import com.hust.baseweb.applications.accounting.document.StatusItem;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.UUID;

/**
 * @author Hien Hoang (hienhoang2702@gmail.com)
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class InvoiceDetailView {

    private String invoiceId;           // varchar(60),
    private InvoiceType invoiceType;      // varchar(60),
    private StatusItem statusId;            // varchar(60),
    private Date invoiceDate;         // TIMESTAMP,
    private UUID toPartyCustomerId; // uuid,
    private UUID fromVendorId;       // uuid,
    private Double amount;               // decimal(18, 2),
    private Double paidAmount;
    private String currencyUomId;      // varchar(60),
}

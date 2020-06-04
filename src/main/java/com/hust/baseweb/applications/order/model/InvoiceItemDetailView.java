package com.hust.baseweb.applications.order.model;

import com.hust.baseweb.applications.accounting.document.InvoiceItemType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Hien Hoang (hienhoang2702@gmail.com)
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class InvoiceItemDetailView {

    private String invoiceId;           // varchar(60),
    private String invoiceItemSeqId;  // varchar(60),
    private InvoiceItemType invoiceItemType; // varchar(60),
    private Double amount;               // decimal(18, 2),
    private String currencyUomId;      // varchar(60),
}

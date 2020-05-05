package com.hust.baseweb.applications.accounting.service;

import com.hust.baseweb.applications.accounting.document.InvoiceItem;

import java.util.List;

/**
 * @author Hien Hoang (hienhoang2702@gmail.com)
 */
public interface InvoiceItemService {
    List<InvoiceItem.Model> findByInvoiceId(String invoiceId);
}

package com.hust.baseweb.applications.accounting.document;

import lombok.Getter;

/**
 * @author Hien Hoang (hienhoang2702@gmail.com)
 */
@Getter
public enum InvoiceType {
    SALES_INVOICE("Hoá đơn bán hàng");

    InvoiceType(String description) {
        this.description = description;
    }

    private String description;
}

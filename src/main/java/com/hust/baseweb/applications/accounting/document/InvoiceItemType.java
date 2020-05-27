package com.hust.baseweb.applications.accounting.document;

import lombok.Getter;

/**
 * @author Hien Hoang (hienhoang2702@gmail.com)
 */
@Getter
public enum InvoiceItemType {
    SALES_INVOICE_PRODUCT_ITEM("Doanh thu bán hàng hóa sản phẩm");

    private String description;

    InvoiceItemType(String description) {

        this.description = description;
    }
}

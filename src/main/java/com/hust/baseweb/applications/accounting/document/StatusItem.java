package com.hust.baseweb.applications.accounting.document;

import lombok.Getter;

/**
 * @author Hien Hoang (hienhoang2702@gmail.com)
 */
public enum StatusItem {
    INVOICE_CREATED("Tạo mới hóa đơn"),
    INVOICE_APPROVED("Đã phê duyệt hóa đơn"),
    INVOICE_CANCELED("Hóa đơn đã bị hủy"),
    INVOICE_COMPLETED("Hóa đơn hoàn thành"),

    PAYMENT_CREATED("Tạo mới khoản thanh toán"),
    PAYMENT_APPROVED("Đã phê duyệt khoản thanh toán"),
    PAYMENT_CANCELED("Khoản thanh toán đã bị hủy"),
    PAYMENT_COMPLETED("Khoản thanh toán hoàn thành");

    @Getter
    private String description;

    StatusItem(String description) {

        this.description = description;
    }
}

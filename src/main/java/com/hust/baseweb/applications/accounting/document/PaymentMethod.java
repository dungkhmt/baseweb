package com.hust.baseweb.applications.accounting.document;

import lombok.Getter;

/**
 * @author Hien Hoang (hienhoang2702@gmail.com)
 */
@Getter
public enum PaymentMethod {
    CASH("Tiền mặt"),
    BANK("Chuyển khoản");

    private String description;

    PaymentMethod(String description) {
        this.description = description;
    }
}

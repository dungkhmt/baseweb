package com.hust.baseweb.applications.accounting.document;

import lombok.Getter;

/**
 * @author Hien Hoang (hienhoang2702@gmail.com)
 */
@Getter
public enum PaymentType {
    CUSTOMER_PAYMENT("Khách hàng thanh toán"),
    COMPANY_PAYMENT("Công ty thanh toán");

    private String description;

    PaymentType(String description) {

        this.description = description;
    }
}

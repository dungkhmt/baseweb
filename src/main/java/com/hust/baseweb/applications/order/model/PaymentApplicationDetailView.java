package com.hust.baseweb.applications.order.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

/**
 * @author Hien Hoang (hienhoang2702@gmail.com)
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PaymentApplicationDetailView {

    private String paymentId;             // varchar(60),
    private String invoiceId;             // varchar(60),
    private Double appliedAmount;         // decimal(18, 2),
    private String currencyUomId;        // varchar(60),
    private Date effectiveDate;         // timestamp,
}

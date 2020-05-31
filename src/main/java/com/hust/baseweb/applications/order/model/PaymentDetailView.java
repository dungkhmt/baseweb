package com.hust.baseweb.applications.order.model;

import com.hust.baseweb.applications.accounting.document.PaymentMethod;
import com.hust.baseweb.applications.accounting.document.PaymentType;
import com.hust.baseweb.applications.accounting.document.StatusItem;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import java.util.Date;
import java.util.UUID;

/**
 * @author Hien Hoang (hienhoang2702@gmail.com)
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PaymentDetailView {

    @Id
    private String paymentId;         // varchar(60),
    private PaymentType paymentType;    // varchar(60),
    private PaymentMethod paymentMethod;  // varchar(60),
    private UUID fromCustomerId;      // uuid,
    private UUID toVendorId;        // uuid,
    private Double amount;             // decimal(18, 2),
    private Double appliedAmount;         // decimal(18, 2),
    private String currencyUomId;    // varchar(60),
    private Date effectiveDate;     // timestamp,
    private StatusItem statusId;          // varchar(60),
}

package com.hust.baseweb.applications.accounting.document;

import com.hust.baseweb.utils.Constant;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * @author Hien Hoang (hienhoang2702@gmail.com)
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Document
public class Payment {
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
    private Date lastUpdatedStamp; // timestamp,
    private Date createdStamp;      // timestamp default current_timestamp,

    public static String convertSequenceIdToPaymentId(Long id) {
        return "PAY" + String.format("%010d", id);
    }

    public Model toModel() {
        return new Model(
                paymentId,
                paymentType.toString(),
                paymentMethod.toString(),
                Optional.ofNullable(fromCustomerId).map(UUID::toString).orElse(null),
                Optional.ofNullable(toVendorId).map(UUID::toString).orElse(null),
                amount,
                appliedAmount,
                currencyUomId,
                Constant.DATE_FORMAT.format(effectiveDate),
                statusId.toString()
        );
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    public static class Model {
        private String paymentId;         // varchar(60),
        private String paymentTypeId;    // varchar(60),
        private String paymentMethodId;  // varchar(60),
        private String fromCustomerId;      // uuid,
        private String toVendorId;        // uuid,
        private Double amount;             // decimal(18, 2),
        private Double appliedAmount;         // decimal(18, 2),
        private String currencyUomId;    // varchar(60),
        private String effectiveDate;     // timestamp,
        private String statusId;          // varchar(60),
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    public static class ApplicationModel {
        private Model payment;
        private List<PaymentApplication.Model> paymentApplications;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    public static class CreateModel {
        private String partyId;
        private Double amount;
    }
}

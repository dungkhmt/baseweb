package com.hust.baseweb.applications.accounting.document;

import com.hust.baseweb.utils.Constant;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.Date;

/**
 * @author Hien Hoang (hienhoang2702@gmail.com)
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Document
public class PaymentApplication {
    @MongoId
    private ObjectId paymentApplicationId;
    private String paymentId;             // varchar(60),
    private String invoiceId;             // varchar(60),
    private Double appliedAmount;         // decimal(18, 2),
    private String currencyUomId;        // varchar(60),
    private Date effectiveDate;         // timestamp,
    private Date lastUpdatedStamp;     // timestamp,
    private Date createdStamp;          // timestamp     default current_timestamp,

    public Model toModel() {
        return new Model(
            paymentApplicationId.toString(),
            paymentId,
            invoiceId,
            appliedAmount,
            currencyUomId,
            Constant.DATE_FORMAT.format(effectiveDate)
        );
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    public static class Model {
        private String paymentApplicationId; // uuid not null default uuid_generate_v1(),
        private String paymentId;             // varchar(60),
        private String invoiceId;             // varchar(60),
        private Double appliedAmount;         // decimal(18, 2),
        private String currencyUomId;        // varchar(60),
        private String effectiveDate;         // timestamp,
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    public static class CreateModel {
        private String paymentId;
        private String invoiceId;
        private Double amount;
    }
}

package com.hust.baseweb.applications.accounting.document;

import com.hust.baseweb.utils.Constant;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;
import java.util.Date;
import java.util.UUID;

/**
 * @author Hien Hoang (hienhoang2702@gmail.com)
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Document
public class Invoice {

    @Id
    private String invoiceId;           // varchar(60),
    private InvoiceType invoiceType;      // varchar(60),
    private String statusId;            // varchar(60),
    private Date invoiceDate;         // TIMESTAMP,
    private UUID toPartyCustomerId; // uuid,
    private UUID fromVendorId;       // uuid,
    private Double amount;               // decimal(18, 2),
    private String currencyUomId;      // varchar(60),
    private Date lastUpdatedStamp;   // TIMESTAMP,
    private Date createdStamp;        // TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    public Model toModel() {
        return new Model(
                invoiceId,
                invoiceType.toString(),
                statusId,
                Constant.DATE_FORMAT.format(invoiceDate),
                toPartyCustomerId.toString(),
                fromVendorId.toString(),
                amount,
                currencyUomId
        );
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    public static class Model {
        private String invoiceId;           // varchar(60),
        private String invoiceType;      // varchar(60),
        private String statusId;            // varchar(60),
        private String invoiceDate;         // TIMESTAMP,
        private String toPartyCustomerId; // uuid,
        private String fromVendorId;       // uuid,
        private Double amount;               // decimal(18, 2),
        private String currencyUomId;      // varchar(60),
    }
}

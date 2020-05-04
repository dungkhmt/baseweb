package com.hust.baseweb.applications.accounting.document;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * @author Hien Hoang (hienhoang2702@gmail.com)
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Document
public class InvoiceItem {

    @javax.persistence.Id
    private Id id;
    private String invoiceItemTypeId; // varchar(60),
    private Double amount;               // decimal(18, 2),
    private String currencyUomId;      // varchar(60),
    private Date lastUpdatedStamp;   // TIMESTAMP,
    private Date createdStamp;        // TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    public static class Id {
        private String invoiceId;           // varchar(60),
        private String invoiceItemSeqId;  // varchar(60),
    }
}

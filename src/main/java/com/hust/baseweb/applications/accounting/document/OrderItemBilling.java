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
public class OrderItemBilling {

    @javax.persistence.Id
    private Id id;
    private Integer quantity;            // int,
    private Double amount;              // decimal(18, 2),
    private Date lastUpdatedStamp;  // TIMESTAMP,
    private Date createdStamp;       // TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    public static class Id {
        private String orderId;            // varchar(60),
        private String orderItemSeqId;   // varchar(60),
        private String invoiceId;          // varchar(60),
        private String invoiceItemSeqId; // varchar(60),
    }
}

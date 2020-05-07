package com.hust.baseweb.applications.accounting.document;

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
public class InvoiceStatus {
    @MongoId
    private ObjectId invoiceStatusId;
    private String invoiceId;         // varchar(60),
    private StatusItem statusId;          // varchar(60),
    private Date fromDate;        // timestamp,
    private Date thruDate;        // timestamp,
    private Date lastUpdatedStamp; // timestamp,
    private Date createdStamp;      // timestamp     default current_timestamp,
}

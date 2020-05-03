package com.hust.baseweb.applications.accounting.document;

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
public class InvoiceStatus {
    @Id
    private UUID invoiceStatusId;  // uuid not null default uuid_generate_v1(),
    private String invoiceId;         // varchar(60),
    private String statusId;          // varchar(60),
    private Date statusDate;        // timestamp,
    private Date lastUpdatedStamp; // timestamp,
    private Date createdStamp;      // timestamp     default current_timestamp,
}

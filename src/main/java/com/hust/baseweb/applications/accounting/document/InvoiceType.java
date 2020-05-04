package com.hust.baseweb.applications.accounting.document;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;
import java.util.Date;

/**
 * @author Hien Hoang (hienhoang2702@gmail.com)
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Document
public class InvoiceType {
    @Id
    private String invoiceTypeId;    // varchar(60) not null,
    private String description;        // varchar(200),
    private Date lastUpdatedStamp; // TIMESTAMP,
    private Date createdStamp;      // TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
}

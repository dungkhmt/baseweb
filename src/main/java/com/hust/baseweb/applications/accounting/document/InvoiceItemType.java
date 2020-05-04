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
public class InvoiceItemType {
    @Id
    private String invoiceItemTypeId; // varchar(60),
    private String description;          // varchar(200),
    private Date lastUpdatedStamp;   // timestamp,
    private Date createdStamp;        // timestamp DEFAULT CURRENT_TIMESTAMP,
}

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
public class Payment {
    @Id
    private String paymentId;         // varchar(60),
    private String paymentTypeId;    // varchar(60),
    private String paymentMethodId;  // varchar(60),
    private UUID fromPartyId;      // uuid,
    private UUID toPartyId;        // uuid,
    private Double Amount;             // decimal(18, 2),
    private String currencyUomId;    // varchar(60),
    private Date effectiveDate;     // timestamp,
    private String statusId;          // varchar(60),
    private Date lastUpdatedStamp; // timestamp,
    private Date createdStamp;      // timestamp default current_timestamp,
}

package com.hust.baseweb.applications.accounting.document;

import com.hust.baseweb.applications.logistics.entity.Product;
import com.hust.baseweb.utils.Constant;
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

    @org.springframework.data.annotation.Id
    private Id id;
    private InvoiceItemType invoiceItemType; // varchar(60),
    private Double amount;               // decimal(18, 2),
    private String currencyUomId;      // varchar(60),
    private Date lastUpdatedStamp;   // TIMESTAMP,
    private Date createdStamp;        // TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    public Model toModel(Invoice invoice, Product product) {
        return new Model(
                id.invoiceId,
                id.invoiceItemSeqId,
                invoiceItemType.toString(),
                Constant.DATE_FORMAT.format(invoice.getInvoiceDate()),
                amount,
                currencyUomId,
                product.getProductId(),
                product.getProductName()
        );
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    public static class Id {
        private String invoiceId;           // varchar(60),
        private String invoiceItemSeqId;  // varchar(60),
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    public static class Model {
        private String invoiceId;           // varchar(60),
        private String invoiceItemSeqId;  // varchar(60),
        private String invoiceItemType; // varchar(60),
        private String invoiceDate;
        private Double amount;               // decimal(18, 2),
        private String currencyUomId;      // varchar(60),
        private String productId;
        private String productName;
    }
}

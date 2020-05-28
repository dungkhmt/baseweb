package com.hust.baseweb.applications.accounting.document;

import com.hust.baseweb.applications.logistics.entity.Product;
import com.hust.baseweb.applications.order.entity.OrderItem;
import com.hust.baseweb.utils.Constant;
import lombok.*;
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

    public Model toModel(Invoice invoice, OrderItem orderItem) {
        Product product = new Product();
        String orderId = null;
        String orderItemSeqId = null;
        if (orderItem != null) {
            if (orderItem.getProduct() != null) {
                product = orderItem.getProduct();
            }
            orderId = orderItem.getOrderId();
            orderItemSeqId = orderItem.getOrderItemSeqId();
        }
        return new Model(
            id.invoiceId,
            id.invoiceItemSeqId,
            invoiceItemType.toString(),
            Constant.DATE_FORMAT.format(invoice.getInvoiceDate()),
            amount,
            currencyUomId,
            product.getProductId(),
            product.getProductName(),
            orderId,
            orderItemSeqId
        );
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    @EqualsAndHashCode
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
        private String orderId;
        private String orderItemSeqId;
    }
}

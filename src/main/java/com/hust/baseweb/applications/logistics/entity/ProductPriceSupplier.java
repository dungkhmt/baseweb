package com.hust.baseweb.applications.logistics.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

/**
 * @author Hien Hoang (hienhoang2702@gmail.com)
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class ProductPriceSupplier {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "product_price_supplier_id")
    private UUID productPriceSupplierId; // uuid not null default uuid_generate_v1(),

    @JoinColumn(name = "party_supplier_id", referencedColumnName = "party_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Supplier partySupplier; // uuid not null,

    @JoinColumn(name = "product_id", referencedColumnName = "product_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Product product; // varchar(60),

    private Integer unitPrice; // int,
    private Date fromDate; // timestamp,
    private Date thruDate; // timestamp,

    public Model toModel() {
        return new Model(product.getProductId(), product.getProductName(), unitPrice);
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    public static class Model {

        private String productId;
        private String productName;
        private Integer unitPrice;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    public static class SetModel {

        private String supplierPartyId;
        private String productId;
        private Integer unitPrice;
    }
}

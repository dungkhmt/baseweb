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
    @ManyToOne(fetch = FetchType.EAGER)
    private Supplier partySupplier; // uuid not null,

    private String productId; // varchar(60),
    private Integer unitPrice; // int,
    private Date fromDate; // timestamp,
    private Date thruDate; // timestamp,
}

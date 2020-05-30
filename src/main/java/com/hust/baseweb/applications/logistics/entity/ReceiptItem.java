package com.hust.baseweb.applications.logistics.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.UUID;

/**
 * @author Hien Hoang (hienhoang2702@gmail.com)
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class ReceiptItem {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID receiptItemId; // uuid not null default uuid_generate_v1(),

    @JoinColumn(referencedColumnName = "receipt_id", name = "receipt_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Receipt receipt; // varchar(60),

    @JoinColumn(name = "product_id", referencedColumnName = "product_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Product product; // varchar(60),

    private Integer quantity; // int,
}

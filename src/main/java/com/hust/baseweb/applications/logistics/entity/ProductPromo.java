package com.hust.baseweb.applications.logistics.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "product_promo")
public class ProductPromo {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "product_promo_id")
    private UUID productPromoId;

    @Column(name = "promo_name")
    private String promoName;

    @Column(name = "promo_text")
    private String promoText;

    @Column(name = "from_date")
    private Date fromDate;

    @Column(name = "thru_date")
    private Date thruDate;

    private String createdStamp;
    private String lastUpdatedStamp;
}

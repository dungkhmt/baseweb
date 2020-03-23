package com.hust.baseweb.applications.logistics.entity;

import com.hust.baseweb.entity.UserLogin;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Entity
@Getter
@Setter
public class ProductPrice {
    @Id
    @Column(name = "product_price_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID productPriceId;


    @JoinColumn(name = "product_id", referencedColumnName = "product_id")
    @ManyToOne(fetch = FetchType.EAGER)
    private Product product;

    @JoinColumn(name = "currency_uom_id", referencedColumnName = "uom_id")
    @ManyToOne
    private Uom currencyUom;

    @Column(name = "price")
    private Double price;

    @Column(name = "tax_in_price")
    private String taxInPrice;

    @Column(name = "from_date")
    private Date fromDate;

    @Column(name = "thru_date")
    private Date thruDate;

    @JoinColumn(name = "created_by_user_login_id", referencedColumnName = "user_login_id")
    @ManyToOne
    private UserLogin createdByUserLogin;


}

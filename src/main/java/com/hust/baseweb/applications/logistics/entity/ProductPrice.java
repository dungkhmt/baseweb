package com.hust.baseweb.applications.logistics.entity;

import com.hust.baseweb.utils.Constant;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.Optional;
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
    @ManyToOne(fetch = FetchType.LAZY)
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

    //@JoinColumn(name = "created_by_user_login_id", referencedColumnName = "user_login_id")
    //@ManyToOne
    //private UserLogin createdByUserLogin;

    @Column(name = "created_by_user_login_id")
    private String createdByUserLoginId;

    public Model toModel() {
        return new Model(
            product.getProductId(),
            price,
            Optional.ofNullable(currencyUom).map(Uom::getUomId).orElse(null),
            Optional.ofNullable(fromDate).map(Constant.DATE_FORMAT::format).orElse(null),
            Optional.ofNullable(thruDate).map(Constant.DATE_FORMAT::format).orElse(null)
        );
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    public static class Model {

        private String productId;
        private Double price;
        private String currencyUomId;

        private String fromDate;
        private String thruDate;
    }
}

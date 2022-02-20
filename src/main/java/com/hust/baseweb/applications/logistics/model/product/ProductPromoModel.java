package com.hust.baseweb.applications.logistics.model.product;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class ProductPromoModel {
    // promo rule id, promo rule json params, promo name, promo from date, promo thru date
    private UUID productPromoRuleId;
    private Double promoPercentageDiscount;
    private String promoName;
    private String fromDate;
    private String thruDate;
}

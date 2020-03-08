package com.hust.baseweb.applications.logistics.model.product;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter

public class SetProductPriceInputModel {
    private String productId;
    private BigDecimal price;
    private String taxInPrice;
    private String currencyUomId;
}

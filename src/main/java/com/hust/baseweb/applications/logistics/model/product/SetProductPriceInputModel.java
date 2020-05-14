package com.hust.baseweb.applications.logistics.model.product;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class SetProductPriceInputModel {
    private String productId;
    private Double price;
    private String taxInPrice;
    private String currencyUomId;
    private String fromDate;
    private String thruDate;
}

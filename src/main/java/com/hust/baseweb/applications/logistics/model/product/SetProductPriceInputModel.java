package com.hust.baseweb.applications.logistics.model.product;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class SetProductPriceInputModel {
	private String productId;
	private BigDecimal price;
	private String taxInPrice;
	private String currencyUomId;
}

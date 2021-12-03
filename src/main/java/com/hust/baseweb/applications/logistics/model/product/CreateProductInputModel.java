package com.hust.baseweb.applications.logistics.model.product;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateProductInputModel {

    private String productId;
    private String productName;
    private Double weight;
    private String uomId;
    private String productType;
    private String[] fileId;
}

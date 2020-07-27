package com.hust.baseweb.applications.specialpurpose.saleslogmongo.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductModel {
    private String productId;
    private String productName;
    private int unitPrice;
}

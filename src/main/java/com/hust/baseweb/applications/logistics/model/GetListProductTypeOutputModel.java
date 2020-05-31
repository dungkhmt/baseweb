package com.hust.baseweb.applications.logistics.model;


import com.hust.baseweb.applications.logistics.entity.ProductType;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class GetListProductTypeOutputModel {

    private List<ProductType> productTypes;

    public GetListProductTypeOutputModel(List<ProductType> productTypes) {
        this.productTypes = productTypes;
    }
}


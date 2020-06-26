package com.hust.baseweb.applications.logistics.model;

import com.hust.baseweb.applications.logistics.entity.Product;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class GetListProductOutputModel {

    private List<Product> products;

    public GetListProductOutputModel(List<Product> products) {
        super();
        this.products = products;
    }

}

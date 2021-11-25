package com.hust.baseweb.applications.logistics.model.product;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ListProductsByDefinePageModel {
    private List<ProductByDefinePageModel> products;
    private long totalElements;
}

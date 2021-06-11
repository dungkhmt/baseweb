package com.hust.baseweb.applications.specialpurpose.saleslogmongo.service;

import com.hust.baseweb.applications.specialpurpose.saleslogmongo.model.ProductModel;

import java.util.List;

public interface MongoProductService {

    List<ProductModel> findAllProductAndPrice();
}

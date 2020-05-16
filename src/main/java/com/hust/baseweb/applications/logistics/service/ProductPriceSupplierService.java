package com.hust.baseweb.applications.logistics.service;

import com.hust.baseweb.applications.logistics.entity.ProductPriceSupplier;

import java.util.List;

/**
 * @author Hien Hoang (hienhoang2702@gmail.com)
 */
public interface ProductPriceSupplierService {
    List<ProductPriceSupplier.Model> getAllProductPriceSuppliers(String supplierPartyId);

    ProductPriceSupplier setProductPriceSupplier(ProductPriceSupplier.SetModel setModel);
}

package com.hust.baseweb.applications.logistics.service;

import com.hust.baseweb.applications.logistics.entity.Product;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ProductService {
    Product findByProductId(String productId);

    List<Product> getAllProducts();

    Product save(String productId, String productName, String productTransportCategory, double productWeight, String uomId);
}

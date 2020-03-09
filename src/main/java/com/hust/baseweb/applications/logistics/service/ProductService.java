package com.hust.baseweb.applications.logistics.service;

import com.hust.baseweb.applications.logistics.entity.Product;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ProductService {
    public Product findByProductId(String productId);

    public List<Product> getAllProducts();
    
    public Product save(String productId, String productName, String uomId);

    public void saveProduct(Product product);
}

package com.hust.baseweb.applications.logistics.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hust.baseweb.applications.logistics.entity.Product;

@Service
public interface ProductService {
	public Product findByProductId(String productId);
	public List<Product> getAllProducts();
}
